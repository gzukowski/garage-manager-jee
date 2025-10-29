package garagemanager.carparts.model.converter;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.model.CarModel;
import garagemanager.carparts.service.CarService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@FacesConverter(value = "carConverter", managed = true)
public class CarConverter implements Converter<CarModel> {

    private final CarService service;

    private final ModelFunctionFactory factory;

    @Inject
    public CarConverter(CarService service, ModelFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }


    @Override
    public CarModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        Optional<Car> car = service.find(UUID.fromString(value));
        return car.map(factory.carToModel()).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CarModel value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}
