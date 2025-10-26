package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartModel;

import java.io.Serializable;
import java.util.function.Function;

public class PartToModelFunction implements Function<Part, PartModel>, Serializable {

    @Override
    public PartModel apply(Part entity) {
        if (entity == null) {
            return null;
        }

        return PartModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .addedDate(entity.getAddedDate())
                .condition(entity.getCondition())
                .carId(entity.getCar() != null ? entity.getCar().getId() : null)
                .carName(entity.getCar() != null ? entity.getCar().getName() : null)
                .carBrand(entity.getCar() != null ? entity.getCar().getBrand() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getName() : null)
                .build();
    }
}
