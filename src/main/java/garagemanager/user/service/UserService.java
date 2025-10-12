package garagemanager.user.service;

import garagemanager.crypto.component.Pbkdf2PasswordHash;
import garagemanager.user.entity.User;
import garagemanager.user.repository.api.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository repository;

    private final Pbkdf2PasswordHash passwordHash;

    private final Path photoDir;

    public UserService(UserRepository repository, Pbkdf2PasswordHash passwordHash, String  photoDir) {
        this.repository = repository;
        this.passwordHash = passwordHash;

        Path base = Paths.get(photoDir);
        if (!base.isAbsolute()) {
            base = Paths.get(System.getProperty("user.dir")).resolve(photoDir);
        }
        this.photoDir = base.normalize().toAbsolutePath();

        try {
            Files.createDirectories(this.photoDir);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create photo directory: " + this.photoDir, e);
        }
    }

    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    public Optional<User> find(String login) {
        return repository.findByLogin(login);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }

    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

    public void updatePhoto(UUID id, InputStream photo) {
        repository.find(id).ifPresent(user -> {
            try {
                // Unikalna nazwa pliku, np. kevin_3f27a.png
                String fileName = user.getLogin() + "_" + UUID.randomUUID() + ".png";
                Path avatarPath = photoDir.resolve(fileName);

                // Zapis pliku
                Files.createDirectories(photoDir);
                Files.write(avatarPath, photo.readAllBytes());

                // Zapis ścieżki (zawsze absolutna)
                user.setPhotoPath(avatarPath.toString());
                repository.update(user);

            } catch (IOException e) {
                throw new IllegalStateException("Error saving photo for user ID: " + id, e);
            }
        });
    }

    public byte[] getPhoto(UUID id) {
        return repository.find(id).map(user -> {
            String storedPath = user.getPhotoPath();
            if (storedPath == null || storedPath.isBlank()) {
                return new byte[0];
            }

            Path photoPath = Paths.get(storedPath);
            if (!Files.exists(photoPath)) {
                return new byte[0];
            }

            try {
                return Files.readAllBytes(photoPath);
            } catch (IOException e) {
                throw new IllegalStateException("Error reading photo for user ID: " + id, e);
            }
        }).orElse(new byte[0]);
    }

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
