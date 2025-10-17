package garagemanager.carparts.repository.inmemory;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.datastorage.component.DataStorage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class CarInMemRepository implements CarRepository {

    private final DataStorage store;

    @Inject
    public CarInMemRepository(DataStorage store) {
        this.store = store;
    }

    @Override
    public Optional<Car> find(UUID id) {
        return store.findAllCars().stream()
                .filter(car -> car.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Car> findAll() {
        return store.findAllCars();
    }

    @Override
    public void create(Car entity) {
        store.createCar(entity);
    }

    @Override
    public void delete(Car entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public void update(Car entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

}
