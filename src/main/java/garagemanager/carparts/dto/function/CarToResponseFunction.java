package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.response.GetCarResponse;
import garagemanager.carparts.entity.Car;

import java.util.function.Function;

public class CarToResponseFunction implements Function<Car, GetCarResponse> {
    @Override
    public GetCarResponse apply(Car entity) {
        return GetCarResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .productionYear(entity.getProductionYear())
                .mileage(entity.getMileage())
                .fuelType(entity.getFuelType())
                .build();
    }

}
