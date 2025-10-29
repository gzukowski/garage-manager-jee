package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.request.PutCarRequest;
import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.FuelType;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToCarFunction implements BiFunction<UUID, PutCarRequest, Car> {
    @Override
    public Car apply(UUID id, PutCarRequest request) {
        return Car.builder()
                .id(id)
                .name(request.getName())
                .brand(request.getBrand())
                .productionYear(request.getProductionYear())
                .fuelType(request.getFuelType())
                .mileage(request.getMileage())
                .build();
    }
}
