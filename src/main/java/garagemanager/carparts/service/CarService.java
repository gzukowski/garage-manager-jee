package garagemanager.carparts.service;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.repository.api.CarRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class CarService {

    private final CarRepository repository;

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
