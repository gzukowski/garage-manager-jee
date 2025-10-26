package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.CarModel;

import java.io.Serializable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CarToModelFunction implements Function<Car, CarModel>, Serializable {

    @Override
    public CarModel apply(Car entity) {
        return CarModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .productionYear(entity.getProductionYear())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .parts(
                        entity.getParts() != null
                                ? entity.getParts().stream()
                                .map(this::mapPart)
                                .collect(Collectors.toList())
                                : null
                )
                .build();
    }

    private CarModel.Part mapPart(Part part) {
        return CarModel.Part.builder()
                .id(part.getId())
                .name(part.getName())
                .build();
    }
}
