package garagemanager.carparts.service;

import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.User;
import garagemanager.carparts.entity.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PartService {
    private final PartRepository partRepository;

    private final CarRepository carRepository;

    private final UserRepository userRepository;

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

    public void delete(UUID id) {
        partRepository.delete(partRepository.find(id).orElseThrow());
    }

    public void updatePhoto(UUID id, InputStream is) {
        partRepository.find(id).ifPresent(part -> {
            try {
                part.setPhoto(is.readAllBytes());
                partRepository.update(part);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public Optional<List<Part>> findAllByCar(UUID id) {
        return carRepository.find(id)
                .map(partRepository::findAllByCar);
    }

    public Optional<List<Part>> findAllByUser(UUID id) {
        return userRepository.find(id)
                .map(partRepository::findAllByUser);
    }

}
