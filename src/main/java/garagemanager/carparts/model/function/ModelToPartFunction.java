package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartCreateModel;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

public class ModelToPartFunction implements Function<PartCreateModel, Part>, Serializable {

    @Override
    @SneakyThrows
    public Part apply(PartCreateModel model) {
        return Part.builder()
                .id(model.getId())
                .name(model.getName())
                .price(model.getPrice())
                .description(model.getDescription())
                .condition(model.getCondition())
                .addedDate(LocalDateTime.now(ZoneId.of("Europe/Warsaw")))
                .car(Car.builder()
                        .id(model.getCar().getId())
                        .build())
                .build();
    }

}
