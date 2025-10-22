package garagemanager.carparts.service;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.User;
import garagemanager.user.repository.api.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class CarService {

    private final CarRepository repository;
    private final PartRepository partRepository;
    private final UserRepository userRepository;

    @Inject
    public CarService(PartRepository partRepository, CarRepository carRepository, UserRepository userRepository) {
        this.repository = carRepository;
        this.partRepository = partRepository;
        this.userRepository = userRepository;
    }

    public Optional<Car> find(UUID id) {
        return repository.find(id);
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    public void create(Car car) {
        repository.create(car);
    }

    public void update(Car car) {
        repository.update(car);
    }

    public void delete(UUID carId) {
        repository.find(carId).ifPresent(car -> {
            List<Part> partsToDelete = partRepository.findAllByCar(car);
            partsToDelete.forEach(partRepository::delete);

            repository.delete(car);
        });
    }
}
