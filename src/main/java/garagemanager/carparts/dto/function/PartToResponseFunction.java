package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.entity.Part;

import java.util.function.Function;

public class PartToResponseFunction implements Function<Part, GetPartResponse> {
    @Override
    public GetPartResponse apply(Part entity) {
        return GetPartResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .condition(entity.getCondition())
                .car(GetPartResponse.Car.builder()
                        .id(entity.getCar().getId())
                        .name(entity.getCar().getName())
                        .build())
                .build();
    }
}
