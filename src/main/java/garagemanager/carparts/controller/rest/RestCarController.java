package garagemanager.carparts.controller.rest;

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
import garagemanager.user.entity.UserRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
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
import lombok.extern.java.Log;

import java.util.UUID;
import java.util.logging.Level;

@Path("")
@RolesAllowed(UserRoles.USER)
@Log
public class RestCarController implements CarController {

    private CarService service;

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
    public RestCarController(
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @EJB
    public void setService(CarService service) {
        this.service = service;
    }

    @Override
    public GetCarsResponse getCars() {
        return factory.carsToResponse().apply(service.findAll());
    }

    @Override
    public GetCarResponse getCar(UUID id) {
        return service.find(id)
                .map(factory.carToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @SneakyThrows
    public void putCar(UUID id, PutCarRequest request) {
        try {
            service.create(factory.requestToCar().apply(id, request));
            //This can be done with Response builder but requires method different return type.
            response.setHeader("Location", uriInfo.getBaseUriBuilder()
                    .path(CarController.class, "getCar")
                    .build(id)
                    .toString());
            //This can be done with Response builder but requires method different return type.
            //Calling HttpServletResponse#setStatus(int) is ignored.
            //Calling HttpServletResponse#sendError(int) causes response headers and body looking like error.
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        } catch (SecurityException | EJBAccessException e) {
            log.log(Level.WARNING, "Unauthorized: ", e);
            throw new WebApplicationException("Unauthorized", Response.Status.FORBIDDEN);
        }
    }

    @Override
    public void patchCar(UUID id, PatchCarRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> service.update(factory.updateCar().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteCar(UUID id) {
        try {
            service.find(id).ifPresentOrElse(
                    entity -> service.delete(id),
                    () -> {
                        throw new NotFoundException();
                    }
            );
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        } catch (SecurityException | EJBAccessException e) {
            log.log(Level.WARNING, "Unauthorized: ", e);
            throw new WebApplicationException("Unauthorized", Response.Status.FORBIDDEN);
        }
    }


}
