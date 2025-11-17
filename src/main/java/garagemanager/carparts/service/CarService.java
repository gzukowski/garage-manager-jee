package garagemanager.carparts.service;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.repository.api.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class CarService {

    private final CarRepository repository;
    private final SecurityContext securityContext;

    @Inject
    public CarService(CarRepository repository,
                      @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext) {
        this.repository = repository;
        this.securityContext = securityContext;
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<Car> find(UUID id) {
        return repository.find(id);
    }

    @RolesAllowed(UserRoles.USER)
    public List<Car> findAll() {
        return repository.findAll();
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void create(Car car) {
        repository.create(car);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void update(Car car) {
        repository.update(car);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void delete(UUID carId) {
        repository.delete(repository.find(carId).orElseThrow());
    }
}
