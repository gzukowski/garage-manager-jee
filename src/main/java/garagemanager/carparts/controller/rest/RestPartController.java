package garagemanager.carparts.controller.rest;

import garagemanager.carparts.controller.api.PartController;
import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.dto.response.GetPartsResponse;
import garagemanager.carparts.service.PartService;
import garagemanager.component.DtoFunctionFactory;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.UriBuilder;
import lombok.SneakyThrows;

import java.util.UUID;

@Path("")//Annotation required by the specification.
public class RestPartController implements PartController {
    private final PartService service;

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

    /**
     * @param service character service
     * @param factory factory producing functions for conversion between DTO and entities
     * @param uriInfo allows to create {@link UriBuilder} based on current request
     */
    @Inject
    public RestPartController(
            PartService service,
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.service = service;
        this.factory = factory;
        this.uriInfo = uriInfo;
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
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void patchPart(UUID id, PatchPartRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> service.update(factory.updatePart().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deletePart(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }


}
