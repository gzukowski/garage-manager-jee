package garagemanager.carparts.service;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.repository.api.UserRepository;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
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
    private final PartRepository partRepository;

    @Inject
    public CarService(PartRepository partRepository, CarRepository carRepository, UserRepository userRepository) {
        this.repository = carRepository;
        this.partRepository = partRepository;
    }

    public Optional<Car> find(UUID id) {
        return repository.find(id);
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void create(Car car) {
        repository.create(car);
    }

    @Transactional
    public void update(Car car) {
        repository.update(car);
    }

    @Transactional
    public void delete(UUID carId) {
        repository.find(carId).ifPresent(car -> {
            List<Part> partsToDelete = partRepository.findAllByCar(car);
            partsToDelete.forEach(partRepository::delete);

            repository.delete(car);
        });
    }
}
