package garagemanager.user.controller.rest;

import garagemanager.carparts.controller.api.CarController;
import garagemanager.carparts.controller.api.PartController;
import garagemanager.carparts.dto.request.PatchCarRequest;
import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutCarRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetCarResponse;
import garagemanager.carparts.dto.response.GetCarsResponse;
import garagemanager.carparts.service.CarService;
import garagemanager.component.DtoFunctionFactory;
import garagemanager.user.controller.api.UserController;
import garagemanager.user.dto.request.PatchUserRequest;
import garagemanager.user.dto.request.PutPasswordRequest;
import garagemanager.user.dto.request.PutUserRequest;
import garagemanager.user.dto.response.GetUserResponse;
import garagemanager.user.dto.response.GetUsersResponse;
import garagemanager.user.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.UUID;

@Path("")
public class RestUserController implements UserController {

    private final UserService service;

    private final DtoFunctionFactory factory;

    private final UriInfo uriInfo;

    /**
     * Current HTTP Servlet response.
     */
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        //ATM in this implementation only HttpServletRequest can be injected with CDI so JAX-RS injection is used.
        this.response = response;
    }


    @Inject
    public RestUserController(
            UserService service,
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.service = service;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public GetUsersResponse getUsers() {
        var users = service.findAll();
        return factory.usersToResponse().apply(users);
    }

    @Override
    public GetUserResponse getUser(UUID id) {
        return service.find(id)
                .map(factory.userToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @SneakyThrows
    public void putUser(UUID id, PutUserRequest request) {
        try {
            service.create(factory.requestToUser().apply(id, request));
            //This can be done with Response builder but requires method different return type.
            response.setHeader("Location", uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getUser")
                    .build(id)
                    .toString());
            //This can be done with Response builder but requires method different return type.
            //Calling HttpServletResponse#setStatus(int) is ignored.
            //Calling HttpServletResponse#sendError(int) causes response headers and body looking like error.
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
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
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void putUserPassword(UUID id, PutPasswordRequest request) {
        // TODO implement actual logic
        throw new BadRequestException();
    }

    @Override
    public byte[] getUserPhoto(UUID id) {
        return service.find(id)
                .map(entity -> service.getPhoto(id))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putUserPhoto(UUID id, InputStream photo) {
        service.find(id).ifPresentOrElse(
                entity -> {
                    service.updatePhoto(id, photo);
                    //This can be done with Response builder but requires method different return type.
                    response.setHeader("Location", uriInfo.getBaseUriBuilder()
                            .path(UserController.class, "getUserPhoto")
                            .build(id)
                            .toString());
                    //This can be done with Response builder but requires method different return type.
                    //Calling HttpServletResponse#setStatus(int) is ignored.
                    //Calling HttpServletResponse#sendError(int) causes response headers and body looking like error.
                    throw new WebApplicationException(Response.Status.CREATED);
                },
                () -> {
                    throw new NotFoundException();
                }
        );

    }


}
