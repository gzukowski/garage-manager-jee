package garagemanager.user.dto.function;

import garagemanager.user.dto.request.PatchUserRequest;
import garagemanager.user.entity.User;

import java.util.function.BiFunction;

public class RequestToUserUpdateFunction implements BiFunction<User, PatchUserRequest, User> {

    @Override
    public User apply(User entity, PatchUserRequest request) {
        return User.builder()
                .id(entity.getId())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .build();
    }

}
