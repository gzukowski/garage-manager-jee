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

import java.util.UUID;
import java.util.logging.Logger;

public class UserControllerImpl implements UserController {
    private static final Logger LOGGER = Logger.getLogger(UserControllerImpl.class.getName());

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
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetUsersResponse getUsers() {
        return factory.usersToResponse().apply(service.findAll());
    }

    @Override
    public void putUser(UUID id, PutUserRequest request) {
        try {
            service.create(factory.requestToUser().apply(id, request));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException();
        }
    }

    @Override
    public void putUserPassword(UUID id, PutPasswordRequest request) {
        throw new BadRequestException(); //TODO
    }

    @Override
    public void patchUser(UUID id, PatchUserRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> service.update(factory.updateUser().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteUser(UUID id) {
        LOGGER.info(() -> String.format("Attempting to delete user with ID: %s", id));

        var user = service.find(id)
                .orElseThrow(() -> {
                    LOGGER.warning(() -> String.format("User with ID %s not found", id));
                    return new NotFoundException();
                });

        service.delete(user.getId());
        LOGGER.info(() -> String.format("User %s has been deleted successfully", id));
    }
}
