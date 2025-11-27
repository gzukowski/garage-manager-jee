package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartsModel;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PartsToModelFunction implements Function<List<Part>, PartsModel>, Serializable {

    @Override
    public PartsModel apply(List<Part> entities) {
        if (entities == null) {
            return PartsModel.builder().build();
        }

        return PartsModel.builder()
                .parts(
                        entities.stream()
                                .map(this::mapPart)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private PartsModel.Part mapPart(Part entity) {
        return PartsModel.Part.builder()
                .id(entity.getId())
                .name(entity.getName())
                .carName(entity.getCar() != null ? entity.getCar().getName() : null)
                .price(entity.getPrice())
                .condition(entity.getCondition() != null ? entity.getCondition().name() : null)
                .version(entity.getVersion())
                .creationDateTime(entity.getCreationDateTime())
                .lastModifiedDateTime(entity.getLastModifiedDateTime())
                .build();
    }
}
