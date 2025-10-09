package garagemanager.user.service;

import garagemanager.crypto.component.Pbkdf2PasswordHash;
import garagemanager.user.entity.User;
import garagemanager.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository repository;

    private final Pbkdf2PasswordHash passwordHash;

    public UserService(UserRepository repository, Pbkdf2PasswordHash passwordHash) {
        this.repository = repository;
        this.passwordHash = passwordHash;
    }

    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    public Optional<User> find(String login) {
        return repository.findByLogin(login);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }

    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

}
