package garagemanager.carparts.view;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.CarModel;
import garagemanager.carparts.service.CarService;
import garagemanager.carparts.service.PartService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * View bean for rendering single character information.
 */
@ViewScoped
@Named
public class CarView implements Serializable {

    private final CarService service;
    private final PartService partService;
    private final ModelFunctionFactory factory;

    /**
     * Car id.
     */
    @Setter
    @Getter
    private UUID id;


    @Getter
    private CarModel car;


    /**
     * @param service service for managing characters
     * @param factory factory producing functions for conversion between models and entities
     */
    @Inject
    public CarView(CarService service, PartService partService,ModelFunctionFactory factory) {
        this.service = service;
        this.partService = partService;
        this.factory = factory;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached within
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Car> carEntity = service.find(id);

        if (carEntity.isPresent()) {
            List<Part> partList = partService.findAllForCurrentUser();

            this.car = factory.carToModel().apply(carEntity.get());

            Set<UUID> partIds = partList.stream()
                    .map(Part::getId)
                    .collect(Collectors.toSet());

            this.car.setParts(this.car.getParts().stream()
                    .filter(p -> partIds.contains(p.getId()))
                    .collect(Collectors.toList()));
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
        }
    }

    public void deletePartAction(CarModel.Part part) {
        try {
            partService.delete(part.getId());
            if (this.car != null && this.car.getParts() != null) {
                this.car.setParts(
                        this.car.getParts().stream()
                                .filter(p -> !p.getId().equals(part.getId()))
                                .collect(Collectors.toList())
                );
            }
            //return "car_view?faces-redirect=true&amp;id=" + id.toString();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

}
