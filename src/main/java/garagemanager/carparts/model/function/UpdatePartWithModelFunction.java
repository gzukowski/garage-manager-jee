package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartEditModel;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.function.BiFunction;

public class UpdatePartWithModelFunction implements BiFunction<Part, PartEditModel, Part>, Serializable {

    @Override
    @SneakyThrows
    public Part apply(Part entity, PartEditModel request) {
        return Part.builder()
                .id(entity.getId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .addedDate(entity.getAddedDate())
                .condition(entity.getCondition())
                .car(entity.getCar())
                .user(entity.getUser())
                .version(request.getVersion())
                .creationDateTime(entity.getCreationDateTime())
                .lastModifiedDateTime(entity.getLastModifiedDateTime())
                .build();
    }
}

