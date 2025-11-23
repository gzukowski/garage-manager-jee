package garagemanager.carparts.model.function;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.CarCreateModel;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.function.Function;

public class ModelToCarFunction implements Function<CarCreateModel, Car>, Serializable {

    @Override
    @SneakyThrows
    public Car apply(CarCreateModel model) {
        return Car.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

}
