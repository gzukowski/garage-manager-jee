package garagemanager.carparts.controller.rest;

import garagemanager.carparts.controller.api.PartController;
import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.dto.response.GetPartsResponse;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.service.CarService;
import garagemanager.carparts.service.PartService;
import garagemanager.component.DtoFunctionFactory;
import garagemanager.user.entity.UserRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.UriBuilder;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.UUID;
import java.util.logging.Level;

@Path("")//Annotation required by the specification.
@RolesAllowed(UserRoles.USER)
@Log
public class RestPartController implements PartController {
    private PartService service;

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
    public RestPartController(
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @EJB
    public void setService(PartService service) {
        this.service = service;
    }

    @Override
    public GetPartsResponse getParts() {
        return factory.partsToResponse().apply(service.findAll());
    }

    @Override
    public GetPartsResponse getCarParts(UUID id) {
        return service.findAllByCar(id)
                .map(factory.partsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPartsResponse getUserParts(UUID id) {
        return service.findAllByUser(id)
                .map(factory.partsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPartResponse getPart(UUID id) {
        return service.find(id)
                .map(factory.partToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @SneakyThrows
    public void putPart(UUID car_id, UUID part_id, PutPartRequest request) {
        try {
            service.create(factory.requestToPart().apply(car_id, part_id, request));
            //This can be done with Response builder but requires method different return type.
            response.setHeader("Location", uriInfo.getBaseUriBuilder()
                    .path(PartController.class, "getPart")
                    .build(part_id)
                    .toString());
            //This can be done with Response builder but requires method different return type.
            //Calling HttpServletResponse#setStatus(int) is ignored.
            //Calling HttpServletResponse#sendError(int) causes response headers and body looking like error.
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (EJBException ex) {
            //Any unchecked exception is packed into EJBException. Business exception can be introduced here.
            if (ex.getCause() instanceof IllegalArgumentException) {
                log.log(Level.WARNING, ex.getMessage(), ex);
                throw new BadRequestException(ex);
            }
            throw ex;
        } catch (SecurityException e) {
            log.log(Level.WARNING, "Unauthorized: ", e);
            throw new WebApplicationException("Unauthorized", Response.Status.FORBIDDEN);
        }
    }

    @Override
    public void patchPart(UUID id, PatchPartRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> {
                    try {
                        Part m = factory.updatePart().apply(entity, request);
                        service.update(m);
                    } catch (EJBAccessException ex) {
                        log.log(Level.WARNING, ex.getMessage(), ex);
                        throw new ForbiddenException(ex.getMessage());
                    }
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deletePart(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> {
                    try {
                        service.delete(id);
                    } catch (EJBAccessException ex) {
                        log.log(Level.WARNING, ex.getMessage(), ex);
                        throw new ForbiddenException(ex.getMessage());
                    }
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }


}
