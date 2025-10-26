package garagemanager.carparts.model.converter;

import garagemanager.carparts.model.CarModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.UUID;

@ApplicationScoped
@FacesConverter(value = "carConverter", managed = true)
public class CarConverter implements Converter<CarModel> {

    @Override
    public CarModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        CarModel car = new CarModel();
        car.setId(UUID.fromString(value));
        return car;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CarModel value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}
