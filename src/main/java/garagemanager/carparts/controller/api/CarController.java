package garagemanager.carparts.controller.api;

import garagemanager.carparts.dto.request.PatchCarRequest;
import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutCarRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetCarResponse;
import garagemanager.carparts.dto.response.GetCarsResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("")
public interface CarController {

    @GET
    @Path("/cars")
    @Produces(MediaType.APPLICATION_JSON)
    GetCarsResponse getCars();

    @GET
    @Path("/cars/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetCarResponse getCar(@PathParam("id") UUID id);

    @PUT
    @Path("/cars/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putCar(@PathParam("id") UUID id, PutCarRequest request);

    @PATCH
    @Path("/cars/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchCar(@PathParam("id")UUID id, PatchCarRequest request);

    @DELETE
    @Path("/cars/{id}")
    void deleteCar(@PathParam("id") UUID id);


}
