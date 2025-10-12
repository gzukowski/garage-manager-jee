package garagemanager.user.controller.impl;

import garagemanager.component.DtoFunctionFactory;
import garagemanager.controller.servlet.exception.BadRequestException;
import garagemanager.controller.servlet.exception.NotFoundException;
import garagemanager.user.controller.api.UserController;
import garagemanager.user.dto.request.PatchUserRequest;
import garagemanager.user.dto.request.PutPasswordRequest;
import garagemanager.user.dto.request.PutUserRequest;
import garagemanager.user.dto.response.GetUserResponse;
import garagemanager.user.dto.response.GetUsersResponse;
import garagemanager.user.service.UserService;
import garagemanager.user.entity.User;

import java.io.InputStream;
import java.util.UUID;

public class UserControllerImpl implements UserController {

    private final UserService service;
    private final DtoFunctionFactory factory;

    public UserControllerImpl(UserService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public GetUserResponse getUser(UUID id) {
        return service.find(id)
                .map(factory.userToResponse())
                .orElseThrow(() -> {
                    return new NotFoundException();
                });
    }

    @Override
    public GetUsersResponse getUsers() {
        var users = service.findAll();
        return factory.usersToResponse().apply(users);
    }

    @Override
    public void putUser(UUID id, PutUserRequest request) {
        try {
            service.create(factory.requestToUser().apply(id, request));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException();
        } catch (Exception exception) {
            throw new BadRequestException();
        }
    }

    @Override
    public void putUserPassword(UUID id, PutPasswordRequest request) {
        // TODO implement actual logic
        throw new BadRequestException();
    }

    @Override
    public void patchUser(UUID id, PatchUserRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> {
                    service.update(factory.updateUser().apply(entity, request));
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteUser(UUID id) {
        var user = service.find(id)
                .orElseThrow(NotFoundException::new);

        try {
            service.delete(user.getId());
        } catch (Exception exception) {
            throw new BadRequestException();
        }
    }

    @Override
    public byte[] getUserPhoto(UUID id) {
        return service.find(id)
                .map(User::getPhoto)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putUserPhoto(UUID id, InputStream photo) {
        service.find(id).ifPresentOrElse(
                entity -> service.updatePhoto(id, photo),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

}
