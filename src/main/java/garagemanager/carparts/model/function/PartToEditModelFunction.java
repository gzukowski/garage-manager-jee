package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartEditModel;

import java.io.Serializable;
import java.util.function.Function;

public class PartToEditModelFunction implements Function<Part, PartEditModel>, Serializable {

    @Override
    public PartEditModel apply(Part entity) {
        return PartEditModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .build();
    }

}

