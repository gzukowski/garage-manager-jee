package garagemanager.user.repository.inmemory;

import garagemanager.datastorage.component.DataStorage;
import garagemanager.user.entity.User;
import garagemanager.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserInMemRepository implements UserRepository {

    private final DataStorage store;

    public UserInMemRepository(DataStorage store) {
        this.store = store;
    }

    @Override
    public Optional<User> find(UUID id) {
        return store.findAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return store.findAllUsers();
    }

    @Override
    public void create(User entity) {
        store.createUser(entity);
    }

    @Override
    public void delete(User entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void update(User entity) {
        store.updateUser(entity);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return store.findAllUsers().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

}

