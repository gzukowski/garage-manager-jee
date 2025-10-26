package garagemanager.carparts.service;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.User;
import garagemanager.carparts.entity.Part;
import garagemanager.user.repository.api.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class PartService {
    private final PartRepository partRepository;

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    @Inject
    public PartService(PartRepository partRepository, CarRepository carRepository, UserRepository userRepository) {
        this.partRepository = partRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public Optional<Part> find(UUID id) {
        return partRepository.find(id);
    }

    public Optional<Part> find(User user, UUID id) {
        return partRepository.findByIdAndUser(id, user);
    }

    public List<Part> findAll() {
        return partRepository.findAll();
    }

    public List<Part> findAll(User user) {
        return partRepository.findAllByUser(user);
    }

    public void create(Part part) {
        partRepository.create(part);
    }

    public void update(Part part) {
        partRepository.update(part);
    }

    public Optional<List<Part>> findAllByCar(UUID id) {
        return carRepository.find(id)
                .map(partRepository::findAllByCar);
    }

    public Optional<List<Part>> findAllByUser(UUID id) {
        return userRepository.find(id)
                .map(partRepository::findAllByUser);
    }

    public void create(Part part, Car car, User user) {
        Car existingCar = carRepository.find(car.getId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono samochodu."));
        User existingUser = userRepository.find(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));

        part.setCar(existingCar);
        part.setUser(existingUser);

        System.out.println("Part " + part);

        if (part.getAddedDate() == null) {
            part.setAddedDate(LocalDateTime.now());
        }

        partRepository.create(part);
    }

    public void delete(UUID id) {
        partRepository.find(id).ifPresent(partRepository::delete);
    }

}
