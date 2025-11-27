package garagemanager.carparts.repository.api;

import garagemanager.repository.Repository;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartRepository extends Repository<Part, UUID> {

    Optional<Part> findByIdAndUser(UUID id, User user);
    List<Part> findAllByUser(User user);
    List<Part> findAllByCar(Car car);
    List<Part> findAllByUserAndCar(User user, Car car);

}
