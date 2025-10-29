package garagemanager.carparts.controller.rest;

import garagemanager.carparts.controller.api.CarController;
import garagemanager.carparts.dto.response.GetCarResponse;
import garagemanager.carparts.dto.response.GetCarsResponse;
import garagemanager.carparts.service.CarService;
import garagemanager.component.DtoFunctionFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("")
public class RestCarController implements CarController {

    private final CarService service;

    /**
     * Factory producing functions for conversion between DTO and entities.
     */
    private final DtoFunctionFactory factory;

    /**
     * @param service profession service
     * @param factory factory producing functions for conversion between DTO and entities
     */
    @Inject
    public RestCarController(CarService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public GetCarsResponse getCars() {
        return factory.carsToResponse().apply(service.findAll());
    }

    @Override
    public GetCarResponse getCar(UUID id) {
        return service.find(id)
                .map(factory.carToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteCar(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }


}
