package garagemanager.carparts.dto.function;

import garagemanager.carparts.dto.response.GetCarsResponse;
import garagemanager.carparts.entity.Car;

import java.util.List;
import java.util.function.Function;

public class CarsToResponseFunction implements Function<List<Car>, GetCarsResponse> {
    @Override
    public GetCarsResponse apply(List<Car> entities) {
        return GetCarsResponse.builder()
                .cars(entities.stream()
                        .map(car -> GetCarsResponse.Car.builder()
                                .id(car.getId())
                                .name(car.getName())
                                .productionYear(car.getProductionYear())
                                .build())
                        .toList())
                .build();
    }
}
