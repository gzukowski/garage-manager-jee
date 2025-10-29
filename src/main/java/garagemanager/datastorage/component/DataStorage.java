package garagemanager.datastorage.component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.serialization.component.CloningUtility;
import garagemanager.user.entity.User;

import java.util.*;
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

    // === CARS ===

    public synchronized List<Car> findAllCars() {
        return cars.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createCar(Car value) {
        if (cars.stream().anyMatch(car -> car.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The car id \"%s\" is not unique".formatted(value.getId()));
        }

        Car entity = cloningUtility.clone(value);
        if (entity.getParts() == null) {
            entity.setParts(new ArrayList<>());
        }

        cars.add(entity);
    }

    public synchronized void updateCar(Car value) {
        Car existing = cars.stream()
                .filter(car -> car.getId().equals(value.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The car with id \"%s\" does not exist".formatted(value.getId())));

        existing.setName(value.getName());
        existing.setBrand(value.getBrand());
        existing.setProductionYear(value.getProductionYear());
        existing.setFuelType(value.getFuelType());
        existing.setMileage(value.getMileage());
        if (value.getParts() != null) {
            existing.setParts(value.getParts());
        }
    }

    public synchronized void deleteCar(UUID id) {
        Car carToDelete = cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The car with id \"%s\" does not exist".formatted(id)));

        parts.removeIf(part -> part.getCar() != null && part.getCar().getId().equals(id));
        cars.remove(carToDelete);
    }

    // === PARTS ===

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

        if (entity.getCar() != null) {
            cars.stream()
                    .filter(car -> car.getId().equals(entity.getCar().getId()))
                    .findFirst()
                    .ifPresent(car -> car.getParts().add(entity));
        }
    }

    public synchronized void updatePart(Part value) {
        Part entity = cloneWithRelationships(value);
        Part existing = parts.stream()
                .filter(part -> part.getId().equals(value.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "The part with id \"%s\" does not exist".formatted(value.getId())));

        if (existing.getCar() != null) {
            cars.stream()
                    .filter(car -> car.getId().equals(existing.getCar().getId()))
                    .findFirst()
                    .ifPresent(car -> car.getParts().removeIf(p -> p.getId().equals(existing.getId())));
        }

        parts.remove(existing);
        parts.add(entity);

        if (entity.getCar() != null) {
            cars.stream()
                    .filter(car -> car.getId().equals(entity.getCar().getId()))
                    .findFirst()
                    .ifPresent(car -> car.getParts().add(entity));
        }

    }

    public synchronized void deletePart(UUID id) {
        Part partToDelete = parts.stream()
                .filter(part -> part.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The part with id \"%s\" does not exist".formatted(id)));

        if (partToDelete.getCar() != null) {
            cars.stream()
                    .filter(car -> car.getId().equals(partToDelete.getCar().getId()))
                    .findFirst()
                    .ifPresent(car -> car.getParts().removeIf(p -> p.getId().equals(id)));
        }

        parts.remove(partToDelete);
    }

    // === USERS ===

    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createUser(User value) {
        if (users.stream().anyMatch(user -> user.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    public synchronized void updateUser(User value) {
        users.removeIf(u -> u.getId().equals(value.getId()));
        users.add(cloningUtility.clone(value));
    }

    public synchronized void deleteUser(UUID id) {
        User userToDelete = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(id)));

        List<Part> userParts = parts.stream()
                .filter(part -> part.getUser() != null && part.getUser().getId().equals(id))
                .collect(Collectors.toList());

        parts.removeAll(userParts);
        users.remove(userToDelete);
    }

    private Part cloneWithRelationships(Part value) {
        System.out.println("[cloneWithRelationships] Starting to clone part: " + value);

        Part entity = cloningUtility.clone(value);
        System.out.println("[cloneWithRelationships] Created clone: " + entity);

        if (entity.getUser() != null) {
            System.out.println("[cloneWithRelationships] Looking for user with ID: " + entity.getUser().getId());
            entity.setUser(users.stream()
                    .filter(user -> user.getId().equals(entity.getUser().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No user with id \"%s\".".formatted(entity.getUser().getId()))));
            System.out.println("[cloneWithRelationships] Set user: " + entity.getUser());
        } else {
            System.out.println("[cloneWithRelationships] No related user found.");
        }

        if (entity.getCar() != null) {
            System.out.println("[cloneWithRelationships] Looking for car with ID: " + entity.getCar().getId());
            entity.setCar(cars.stream()
                    .filter(car -> car.getId().equals(entity.getCar().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No car with id \"%s\".".formatted(entity.getCar().getId()))));
            System.out.println("[cloneWithRelationships] Set car: " + entity.getCar());
        } else {
            System.out.println("[cloneWithRelationships] No related car found.");
        }

        System.out.println("[cloneWithRelationships] Returning cloned entity: " + entity);
        return entity;
    }

}
