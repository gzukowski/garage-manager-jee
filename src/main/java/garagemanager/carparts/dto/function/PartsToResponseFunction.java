package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.response.GetPartsResponse;
import garagemanager.carparts.entity.Part;

import java.util.List;
import java.util.function.Function;

public class PartsToResponseFunction implements Function<List<Part>, GetPartsResponse> {
    @Override
    public GetPartsResponse apply(List<Part> entities) {
        return GetPartsResponse.builder()
                .parts(entities.stream()
                        .map(part -> GetPartsResponse.Part.builder()
                                .id(part.getId())
                                .name(part.getName())
                                .build())
                        .toList())
                .build();
    }
}
