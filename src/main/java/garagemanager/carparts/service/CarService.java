package garagemanager.carparts.service;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.repository.api.CarRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class CarService {

    private final CarRepository repository;

    @Inject
    public CarService(CarRepository repository) {
        this.repository = repository;
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

}
