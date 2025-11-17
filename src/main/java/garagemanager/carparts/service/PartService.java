package garagemanager.carparts.service;

import garagemanager.auth.SecurityUtils;
import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.User;
import garagemanager.carparts.entity.Part;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.repository.api.UserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class PartService {
    private final PartRepository partRepository;

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final SecurityContext securityContext;

    @Inject
    public PartService(PartRepository partRepository,
                       CarRepository carRepository,
                       UserRepository userRepository,
                       @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext
    ) {
        this.partRepository = partRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<Part> find(UUID id) {
        Optional<Part> part = partRepository.find(id);

        SecurityUtils.checkOwnership(
                part,
                p -> p.getUser().getLogin(),
                securityContext
        );

        return part;
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<Part> find(User user, UUID id) {
        Optional<Part> part = partRepository.findByIdAndUser(id, user);

        SecurityUtils.checkOwnership(
                part,
                p -> p.getUser().getLogin(),
                securityContext
        );

        return part;
    }

    @RolesAllowed(UserRoles.USER)
    public List<Part> findAll() {
        return partRepository.findAll();
    }

    @RolesAllowed(UserRoles.USER)
    public List<Part> findAll(User user) {
        return partRepository.findAllByUser(user);
    }

    @RolesAllowed(UserRoles.USER)
    public List<Part> findAllForCurrentUser() {
        var principal = securityContext.getCallerPrincipal();
        if (principal == null) {
            throw new EJBAccessException("Brak zalogowanego użytkownika.");
        }

        boolean isAdmin = securityContext.isCallerInRole(UserRoles.ADMIN);

        if (isAdmin) {
            return partRepository.findAll();
        }

        Optional<User> user = userRepository.findByLogin(principal.getName());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Użytkownik nie istnieje.");
        }

        return partRepository.findAllByUser(user.get());
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<List<Part>> findAllByCar(UUID id) {
        return carRepository.find(id)
                .map(partRepository::findAllByCar);
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<List<Part>> findAllByUser(UUID id) {
        return userRepository.find(id)
                .map(partRepository::findAllByUser);
    }

    @RolesAllowed(UserRoles.USER)
    public void update(Part part) {
        SecurityUtils.checkOwnership(
                Optional.ofNullable(part),
                p -> p.getUser().getLogin(),
                securityContext
        );
        partRepository.update(part);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void create(Part part) {
        System.out.println("Part " + part);

        if (partRepository.find(part.getId()).isPresent()) {
            throw new IllegalArgumentException("Part already exists.");
        }

        if (userRepository.find(part.getUser().getId()).isEmpty()) {
            throw new IllegalArgumentException("User does not exists.");
        }

        if (carRepository.find(part.getCar().getId()).isEmpty()) {
            throw new IllegalArgumentException("Car does not exists.");
        }
        partRepository.create(part);

        /* Both sides of relationship must be handled (if accessed) because of cache. */
        carRepository.find(part.getCar().getId())
                .ifPresent(c -> c.getParts().add(part));
        userRepository.find(part.getUser().getId())
                .ifPresent(u -> u.getParts().add(part));
    }

    @PermitAll
    public void initCreate(Part part) {
        System.out.println("Init create Part " + part);

        if (partRepository.find(part.getId()).isPresent()) {
            throw new IllegalArgumentException("Part already exists.");
        }

        if (userRepository.find(part.getUser().getId()).isEmpty()) {
            throw new IllegalArgumentException("User does not exists.");
        }

        if (carRepository.find(part.getCar().getId()).isEmpty()) {
            throw new IllegalArgumentException("Car does not exists.");
        }
        partRepository.create(part);

        /* Both sides of relationship must be handled (if accessed) because of cache. */
        carRepository.find(part.getCar().getId())
                .ifPresent(c -> c.getParts().add(part));
        userRepository.find(part.getUser().getId())
                .ifPresent(u -> u.getParts().add(part));
    }

    @RolesAllowed(UserRoles.USER)
    public void delete(UUID id) {
        Optional<Part> optionalPart = partRepository.find(id);
        SecurityUtils.checkOwnership(
                optionalPart,
                p -> p.getUser().getLogin(),
                securityContext
        );

        if  (optionalPart.isPresent()) {
            Part part = optionalPart.get();

            carRepository
                    .find(part.getCar().getId())
                    .ifPresent(car -> car.getParts().remove(part));
            partRepository.delete(part);
        }
    }

}
