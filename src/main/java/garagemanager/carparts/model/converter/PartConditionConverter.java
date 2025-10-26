package garagemanager.carparts.model.converter;

import garagemanager.carparts.entity.PartCondition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@ApplicationScoped
@FacesConverter(value = "partConditionConverter", managed = true)
public class PartConditionConverter implements Converter<PartCondition> {

    @Override
    public PartCondition getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return PartCondition.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, PartCondition value) {
        return (value == null) ? "" : value.name();
    }
}
