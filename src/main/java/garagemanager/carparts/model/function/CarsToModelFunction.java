package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.model.CarsModel;

import java.util.List;
import java.util.function.Function;

public class CarsToModelFunction implements Function<List<Car>, CarsModel> {

    @Override
    public CarsModel apply(List<Car> entity) {
        return CarsModel.builder()
                .cars(entity.stream()
                        .map(car -> CarsModel.Car.builder()
                                .id(car.getId())
                                .name(car.getName())
                                .brand(car.getBrand())
                                .productionYear(car.getProductionYear())
                                .build())
                        .toList())
                .build();
    }
}
