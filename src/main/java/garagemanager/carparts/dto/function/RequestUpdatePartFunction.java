package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.entity.Part;
import java.util.function.BiFunction;

public class RequestUpdatePartFunction implements BiFunction<Part, PatchPartRequest, Part> {
    @Override
    public Part apply(Part entity, PatchPartRequest request) {
        return Part.builder()
                .id(entity.getId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .addedDate(entity.getAddedDate())
                .condition(entity.getCondition())
                .car(entity.getCar())
                .photo(entity.getPhoto())
                .build();
    }

}
