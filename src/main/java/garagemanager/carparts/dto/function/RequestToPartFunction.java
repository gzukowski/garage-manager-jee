package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class RequestToPartFunction implements TriFunction<UUID, UUID, PutPartRequest, Part> {
    @Override
    public Part apply(UUID car_id, UUID part_id, PutPartRequest request) {
        return Part.builder()
                .id(part_id)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .addedDate(LocalDateTime.now(ZoneId.of("Europe/Warsaw")))
                .condition(request.getCondition())
                .car(Car.builder()
                        .id(car_id)
                        .build()
                )
                .build();
    }

}
