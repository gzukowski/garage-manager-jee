package garagemanager.user.controller.rest;

import garagemanager.component.DtoFunctionFactory;
import garagemanager.user.controller.api.UserController;
import garagemanager.user.dto.request.PatchUserRequest;
import garagemanager.user.dto.request.PutUserRequest;
import garagemanager.user.dto.response.GetUserResponse;
import garagemanager.user.dto.response.GetUsersResponse;
import garagemanager.user.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.TransactionalException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;

@Path("")
@Log
public class RestUserController implements UserController {

    private UserService service;

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
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @EJB
    public void setService(UserService userService) {
        this.service = userService;
    }

    @Override
    public GetUsersResponse getUsers() {
        try {
            var users = service.findAll();
            return factory.usersToResponse().apply(users);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);

        } catch (EJBAccessException | SecurityException e) {
            log.log(Level.WARNING, "Unauthorized: ", e);
            throw new WebApplicationException("Unauthorized", Response.Status.FORBIDDEN);
        }
    }

    @Override
    public GetUserResponse getUser(UUID id) {
        try {
            return service.find(id)
                    .map(factory.userToResponse())
                    .orElseThrow(NotFoundException::new);
        } catch (EJBAccessException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public void putUser(UUID id, PutUserRequest request) {

        System.out.println("request put user:" + request);
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
        } catch (TransactionalException ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                log.log(Level.WARNING, "User already exists: " + ex.getMessage(), ex);
                throw new BadRequestException("User already exists", ex);
            }
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.log(Level.WARNING, "User already exists: " + ex.getMessage(), ex);
            throw new BadRequestException("User already exists", ex);
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

//    @Override
//    public void putUserPassword(UUID id, PutPasswordRequest request) {
//        // TODO implement actual logic
//        throw new BadRequestException();
//    }

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
