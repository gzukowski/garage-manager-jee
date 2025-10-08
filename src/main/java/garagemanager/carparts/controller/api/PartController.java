package garagemanager.carparts.controller.api;

import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.dto.response.GetPartsResponse;

import java.io.InputStream;
import java.util.UUID;

public interface  PartController {

    GetPartsResponse getParts();

    GetPartsResponse getCarCharacters(UUID id);

    GetPartsResponse getUserCharacters(UUID id);

    GetPartResponse getPart(UUID uuid);

    void putPart(UUID id, PutPartRequest request);

    void patchPart(UUID id, PatchPartRequest request);

    void deletePart(UUID id);

    byte[] getPartPhoto(UUID id);

    void putCharacterPhoto(UUID id, InputStream portrait);


}
