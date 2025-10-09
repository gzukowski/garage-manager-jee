package garagemanager.user.repository.api;

import garagemanager.repository.Repository;
import garagemanager.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    Optional<User> findByLogin(String login);
    void delete(UUID id);
}
