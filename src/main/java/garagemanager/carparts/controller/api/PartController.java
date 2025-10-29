package garagemanager.carparts.controller.api;

import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.dto.response.GetPartsResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("")
public interface  PartController {

    @GET
    @Path("/parts")
    @Produces(MediaType.APPLICATION_JSON)
    GetPartsResponse getParts();

    @GET
    @Path("/cars/{id}/parts")
    @Produces(MediaType.APPLICATION_JSON)
    GetPartsResponse getCarParts(@PathParam("id") UUID id);

    @GET
    @Path("/users/{id}/parts/")
    @Produces(MediaType.APPLICATION_JSON)
    GetPartsResponse getUserParts(@PathParam("id") UUID id);

    @GET
    @Path("/parts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetPartResponse getPart(@PathParam("id") UUID id);

    @PUT
    @Path("/parts/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putPart(@PathParam("id") UUID id, PutPartRequest request);

    @PATCH
    @Path("/parts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchPart(@PathParam("id")UUID id, PatchPartRequest request);

    @DELETE
    @Path("/parts/{id}")
    void deletePart(@PathParam("id") UUID id);

}
