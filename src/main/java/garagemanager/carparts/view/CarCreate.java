package garagemanager.carparts.view;

import garagemanager.carparts.model.CarCreateModel;
import garagemanager.carparts.service.CarService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.UUID;


@ViewScoped
@Named
@Log
@NoArgsConstructor(force = true)
public class CarCreate implements Serializable {


    private final CarService carService;

    private final ModelFunctionFactory factory;

    @Getter
    private CarCreateModel car;


    @Inject
    public CarCreate(
            CarService carService,
            ModelFunctionFactory factory
    ) {
        this.factory = factory;
        this.carService = carService;
    }


    public void init() {
        if (car == null) {
            car = CarCreateModel.builder()
                    .id(UUID.randomUUID())
                    .build();
        }

    }

    public String cancelAction() {
        return "/part/part_list.xhtml?faces-redirect=true";
    }

    public String saveAction() {
        carService.create(factory.modelToCar().apply(car));
        return "/part/part_list.xhtml?faces-redirect=true";
    }

}
