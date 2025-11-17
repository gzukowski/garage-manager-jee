package garagemanager.user.service;

import garagemanager.auth.SecurityUtils;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.configuration.qualifier.PhotosDir;
import garagemanager.user.entity.User;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.repository.api.UserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class UserService {
    private final UserRepository repository;
    private final PartRepository partRepository;
    private final SecurityContext securityContext;
    private final Pbkdf2PasswordHash passwordHash;

    private final Path photoDir;

    @Inject
    public UserService(
            UserRepository repository,
            PartRepository partRepository,
            @SuppressWarnings("CdiInjectionPointsInspection") Pbkdf2PasswordHash passwordHash,
            @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext
//            @PhotosDir String photoDir
    ) {
        this.repository = repository;
        this.partRepository = partRepository;
        this.passwordHash = passwordHash;
        this.securityContext = securityContext;

        //photoDir = Path.of("photos");
//
//        Path base = Paths.get(photoDir);
//        if (!base.isAbsolute()) {
//            base = Paths.get(System.getProperty("user.dir")).resolve(photoDir);
//        }
        this.photoDir = Path.of("photos");//base.normalize().toAbsolutePath();

        try {
            Files.createDirectories(this.photoDir);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create photo directory: " + this.photoDir, e);
        }
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<User> find(String login) {
        return repository.findByLogin(login);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public List<User> findAll() {
        return repository.findAll();
    }

    @PermitAll
    public void create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    @RolesAllowed(UserRoles.USER)
    public void update(User user) {
        SecurityUtils.checkOwnership(
                Optional.ofNullable(user),
                u -> u.getLogin(),
                securityContext
        );
        repository.update(user);
    }

    @RolesAllowed(UserRoles.USER)
    public void delete(UUID id) {
        repository.find(id).ifPresent(user -> {
            List<Part> userParts = partRepository.findAllByUser(user);
            userParts.forEach(partRepository::delete);

            repository.delete(user);
        });
    }

    @PermitAll
    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

    @RolesAllowed(UserRoles.USER)
    public void updatePhoto(UUID id, InputStream photo) {
        repository.find(id).ifPresent(user -> {
            try {
                String fileName = user.getId() + ".png";
                Path avatarPath = photoDir.resolve(fileName);
                Files.createDirectories(photoDir);
                Files.write(avatarPath, photo.readAllBytes());
                user.setPhotoPath(avatarPath.toString());
                repository.update(user);

            } catch (IOException e) {
                throw new IllegalStateException("Error saving photo for user ID: " + id, e);
            }
        });
    }


    @RolesAllowed(UserRoles.USER)
    public byte[] getPhoto(UUID id) {
        return repository.find(id).map(user -> {
            String storedPath = user.getPhotoPath();
            if (storedPath == null || storedPath.isBlank()) {
                return null;
            }

            Path photoPath = Paths.get(storedPath);
            if (!Files.exists(photoPath)) {
                return null;
            }

            try {
                return Files.readAllBytes(photoPath);
            } catch (IOException e) {
                throw new IllegalStateException("Error reading photo for user ID: " + id, e);
            }
        }).orElse(new byte[0]);
    }

    @RolesAllowed(UserRoles.USER)
    public void deletePhoto(UUID id) {
        repository.find(id).ifPresent(user -> {
            String storedPath = user.getPhotoPath();
            if (storedPath == null || storedPath.isBlank()) return;

            Path avatarPath = Paths.get(storedPath);
            try {
                Files.deleteIfExists(avatarPath);
                user.setPhotoPath("");
                repository.update(user);
            } catch (IOException e) {
                throw new IllegalStateException("Error deleting photo for user ID: " + id, e);
            }
        });
    }
}
