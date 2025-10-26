package garagemanager.carparts.repository.inmemory;

import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.datastorage.component.DataStorage;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.user.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
public class PartInMemRepository implements PartRepository {
    private final DataStorage store;

    @Inject
    public PartInMemRepository(DataStorage store) {
        this.store = store;
    }

    @Override
    public Optional<Part> find(UUID id) {
        return store.findAllParts().stream()
                .filter(part -> part.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Part> findAll() {
        return store.findAllParts();
    }

    @Override
    public void create(Part entity) {
        System.out.println("Repo create " +  entity);
        store.createPart(entity);
    }

    @Override
    public void delete(Part entity) {
        store.deletePart(entity.getId());
    }

    @Override
    public void update(Part entity) {
        System.out.println("Repo update " +  entity);
        store.updatePart(entity);
    }

    @Override
    public Optional<Part> findByIdAndUser(UUID id, User user) {
        return store.findAllParts().stream()
                .filter(part -> part.getUser().equals(user))
                .filter(part -> part.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Part> findAllByUser(User user) {
        return store.findAllParts().stream()
                .filter(part -> user.equals(part.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Part> findAllByCar(Car car) {
        return store.findAllParts().stream()
                .filter(part -> car.equals(part.getCar()))
                .collect(Collectors.toList());
    }

}
