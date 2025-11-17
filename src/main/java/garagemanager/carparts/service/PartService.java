package garagemanager.carparts.service;

import garagemanager.carparts.repository.api.CarRepository;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.User;
import garagemanager.carparts.entity.Part;
import garagemanager.user.repository.api.UserRepository;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@LocalBean
@Stateless
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

    @Transactional
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

    @Transactional
    public void create(Part part) {
        System.out.println("Part " + part);

        if (partRepository.find(part.getId()).isPresent()) {
            throw new IllegalArgumentException("Part already exists.");
        }
//        if (userRepository.find(message.getUser().getId()).isEmpty()) {
//            throw new IllegalArgumentException("User does not exists.");
//        }
        if (carRepository.find(part.getCar().getId()).isEmpty()) {
            throw new IllegalArgumentException("Car does not exists.");
        }
        partRepository.create(part);

        /* Both sides of relationship must be handled (if accessed) because of cache. */
        carRepository.find(part.getCar().getId())
                .ifPresent(c -> c.getParts().add(part));
//      userRepository.find(message.getUser().getId())
//                .ifPresent(u -> u.getMessages().add(message));
    }

    @Transactional
    public void delete(UUID id) {
        Optional<Part> optionalPart = partRepository.find(id);

        if  (optionalPart.isPresent()) {
            Part part = optionalPart.get();

            carRepository
                    .find(part.getCar().getId())
                    .ifPresent(car -> car.getParts().remove(part));
            partRepository.delete(part);
        }
    }

}
