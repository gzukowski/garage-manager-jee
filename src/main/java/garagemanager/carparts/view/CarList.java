package garagemanager.carparts.view;

import garagemanager.carparts.model.CarsModel;
import garagemanager.carparts.service.CarService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@RequestScoped
@Named
public class CarList {


    private final CarService service;
    private CarsModel cars;

    /**
     * Factory producing functions for conversion between models and entities.
     */
    private final ModelFunctionFactory factory;

    @Inject
    public CarList(CarService service, ModelFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    public CarsModel getCars() {
        if (cars == null) {
            cars = factory.carsToModel().apply(service.findAll());
        }
        return cars;
    }

    public String deleteAction(CarsModel.Car car) {
        service.delete(car.getId());
        return "car_list?faces-redirect=true";
    }

}
