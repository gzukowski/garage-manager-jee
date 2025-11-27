package garagemanager.carparts.view;

import garagemanager.carparts.model.CarsModel;
import garagemanager.carparts.service.CarService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@RequestScoped
@Named
public class CarList {


    private CarService service;
    private CarsModel cars;

    /**
     * Factory producing functions for conversion between models and entities.
     */
    private final ModelFunctionFactory factory;

    @EJB
    public void setService(CarService service) {
        this.service = service;
    }

    @Inject
    public CarList(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    public CarsModel getCars() {
        if (cars == null) {
            cars = factory.carsToModel().apply(service.findAll());
        }
        return cars;
    }

    public void deleteAction(CarsModel.Car car) {
        service.delete(car.getId());
        //return "car_list?faces-redirect=true";
        this.cars = null;
    }

}
