package garagemanager.user.controller.api;

import garagemanager.carparts.dto.request.PutCarRequest;
import garagemanager.user.dto.request.PatchUserRequest;
import garagemanager.user.dto.request.PutPasswordRequest;
import garagemanager.user.dto.request.PutUserRequest;
import garagemanager.user.dto.response.GetUserResponse;
import garagemanager.user.dto.response.GetUsersResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;
import java.util.UUID;

@Path("")
public interface UserController {

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    GetUsersResponse getUsers();

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetUserResponse getUser(@PathParam("id") UUID id);

    @PUT
    @Path("/users/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putUser(@PathParam("id") UUID id, PutUserRequest request);

    @PUT
    @Path("/users/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putUserPassword(@PathParam("id") UUID id, PutPasswordRequest request);

    @PATCH
    @Path("/users/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void patchUser(@PathParam("id") UUID id, PatchUserRequest request);

    @DELETE
    @Path("/users/{id}")
    void deleteUser(@PathParam("id") UUID id);

    @GET
    @Path("/users/{id}/photo")
    @Produces("image/png")
    byte[] getUserPhoto(UUID id);

    @PUT
    @Path("/users/{id}/photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void putUserPhoto(UUID id, InputStream photo);

}
