package garagemanager.carparts.controller.api;

import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.dto.response.GetPartsResponse;

import java.io.InputStream;
import java.util.UUID;

public interface  PartController {

    GetPartsResponse getParts();

    GetPartsResponse getCarParts(UUID id);

    GetPartsResponse getUserParts(UUID id);

    GetPartResponse getPart(UUID uuid);

    void putPart(UUID id, PutPartRequest request);

    void patchPart(UUID id, PatchPartRequest request);

    void deletePart(UUID id);
}
