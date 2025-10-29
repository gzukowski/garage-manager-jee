package garagemanager.carparts.view;

import garagemanager.carparts.entity.PartCondition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Getter;

@Named
@ApplicationScoped
@Getter
public class PartConditionView {
    public PartCondition[] getValues() {
        return PartCondition.values();
    }
}