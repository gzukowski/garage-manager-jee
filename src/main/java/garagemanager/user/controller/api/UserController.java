package garagemanager.user.controller.api;

import garagemanager.user.dto.request.PatchUserRequest;
import garagemanager.user.dto.request.PutPasswordRequest;
import garagemanager.user.dto.request.PutUserRequest;
import garagemanager.user.dto.response.GetUserResponse;
import garagemanager.user.dto.response.GetUsersResponse;

import java.io.InputStream;
import java.util.UUID;

public interface UserController {
    GetUsersResponse getUsers();
    GetUserResponse getUser(UUID id);
    void putUser(UUID id, PutUserRequest request);
    void putUserPassword(UUID id, PutPasswordRequest request);
    void patchUser(UUID id, PatchUserRequest request);
    void deleteUser(UUID id);

    byte[] getUserPhoto(UUID id);
    void putUserPhoto(UUID id, InputStream photo);

}
