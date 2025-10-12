package garagemanager.user.dto.function;

import garagemanager.user.dto.request.PutPasswordRequest;
import garagemanager.user.entity.User;

import java.util.function.BiFunction;

public class RequestToPasswordUpdateFunction implements BiFunction<User, PutPasswordRequest, User> {

    @Override
    public User apply(User entity, PutPasswordRequest request) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .birthDate(entity.getBirthDate())
                .login(entity.getLogin())
                .password(request.getPassword())
                .email(entity.getEmail())
                .build();
    }

}
