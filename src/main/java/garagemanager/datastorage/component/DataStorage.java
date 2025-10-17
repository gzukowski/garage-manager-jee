package garagemanager.datastorage.component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.serialization.component.CloningUtility;
import garagemanager.user.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
@ApplicationScoped
@NoArgsConstructor(force = true)
public class DataStorage {

    private final Set<Car> cars = new HashSet<>();
    private final Set<Part> parts = new HashSet<>();
    private final Set<User> users = new HashSet<>();

    private final CloningUtility cloningUtility;

    @Inject
    public DataStorage(CloningUtility cloningUtility) {
        this.cloningUtility = cloningUtility;
    }

    public synchronized List<Car> findAllCars() {
        return cars.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createCar(Car value) throws IllegalArgumentException {
        if (cars.stream().anyMatch(car -> car.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The car id \"%s\" is not unique".formatted(value.getId()));
        }
        cars.add(cloningUtility.clone(value));
    }

        public synchronized List<Part> findAllParts() {
        return parts.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createPart(Part value) throws IllegalArgumentException {
        if (parts.stream().anyMatch(part -> part.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The part id \"%s\" is not unique".formatted(value.getId()));
        }
        Part entity = cloneWithRelationships(value);
        parts.add(entity);
    }

    public synchronized void updatePart(Part value) throws IllegalArgumentException {
        Part entity = cloneWithRelationships(value);
        if (parts.removeIf(part -> part.getId().equals(value.getId()))) {
            parts.add(entity);
        } else {
            throw new IllegalArgumentException("The part with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized void deletePart(UUID id) throws IllegalArgumentException {
        if (!parts.removeIf(part -> part.getId().equals(id))) {
            throw new IllegalArgumentException("The part with id \"%s\" does not exist".formatted(id));
        }
    }

    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createUser(User value) throws IllegalArgumentException {
        if (users.stream().anyMatch(part -> part.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    public synchronized void updateUser(User value) throws IllegalArgumentException {
        if (users.removeIf(Part -> Part.getId().equals(value.getId()))) {
            users.add(cloningUtility.clone(value));
        } else {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized void deleteUser(UUID id) throws IllegalArgumentException {
        if (!users.removeIf(user -> user.getId().equals(id))) {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(id));
        }
    }

    private Part cloneWithRelationships(Part value) {
        Part entity = cloningUtility.clone(value);

        if (entity.getUser() != null) {
            entity.setUser(users.stream()
                    .filter(user -> user.getId().equals(value.getUser().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No user with id \"%s\".".formatted(value.getUser().getId()))));
        }

        if (entity.getCar() != null) {
            entity.setCar(cars.stream()
                    .filter(car -> car.getId().equals(value.getCar().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No car with id \"%s\".".formatted(value.getCar().getId()))));
        }

        return entity;
    }
}
