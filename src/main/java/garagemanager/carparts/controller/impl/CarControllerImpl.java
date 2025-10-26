package garagemanager.carparts.controller.impl;

import garagemanager.carparts.controller.api.CarController;
import garagemanager.carparts.dto.response.GetCarsResponse;
import garagemanager.carparts.service.CarService;
import garagemanager.component.DtoFunctionFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class CarControllerImpl implements CarController {

    private final CarService service;

    private final DtoFunctionFactory factory;

    @Inject
    public CarControllerImpl(CarService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public GetCarsResponse getCars() {
        return factory.carsToResponse().apply(service.findAll());
    }


}
