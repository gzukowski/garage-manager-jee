package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.request.PatchCarRequest;
import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import java.util.function.BiFunction;

public class RequestUpdateCarFunction implements BiFunction<Car, PatchCarRequest, Car> {
    @Override
    public Car apply(Car entity, PatchCarRequest request) {
        return Car.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .productionYear(entity.getProductionYear())
                .fuelType(entity.getFuelType())
                .mileage(request.getMileage())
                .parts(entity.getParts())
                .build();
    }

}
