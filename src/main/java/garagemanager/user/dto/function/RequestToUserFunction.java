package garagemanager.user.dto.function;

import garagemanager.user.dto.request.PutUserRequest;
import garagemanager.user.entity.User;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToUserFunction implements BiFunction<UUID, PutUserRequest, User> {

    @Override
    public User apply(UUID id, PutUserRequest request) {
        return User.builder()
                .id(id)
                .login(request.getLogin())
                .name(request.getName())
                .surname(request.getSurname())
                .birthDate(request.getBirthDate())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();
    }

}
