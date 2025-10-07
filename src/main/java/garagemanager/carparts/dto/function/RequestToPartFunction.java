package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToPartFunction implements BiFunction<UUID, PutPartRequest, Part> {
    @Override
    public Part apply(UUID id, PutPartRequest request) {
        return Part.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .addedDate(LocalDateTime.now(ZoneId.of("Europe/Warsaw")))
                .condition(request.getCondition())
                .car(Car.builder()
                        .id(request.getCar().getId())
                        .build())
                .build();
    }

}
