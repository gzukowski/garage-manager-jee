package garagemanager.component;

import garagemanager.carparts.model.function.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

/**
 * Factor for creating {@link Function} implementation for converting between various objects used in different layers.
 * Instead of injecting multiple function objects single factory is injected.
 */
@ApplicationScoped
public class ModelFunctionFactory {


    public CarToModelFunction carToModel() {
        return new CarToModelFunction();
    }

    public CarsToModelFunction carsToModel() {
        return new CarsToModelFunction();
    }

    public PartToModelFunction partToModel() {
        return new PartToModelFunction();
    }

    public PartsToModelFunction partsToModel() {
        return new PartsToModelFunction();
    }

    public PartToEditModelFunction partToEditModel() {
        return new PartToEditModelFunction();
    }

    public UpdatePartWithModelFunction updatePart() {
        return new UpdatePartWithModelFunction();
    }


    public ModelToPartFunction modelToPart() {
        return new ModelToPartFunction();
    }
//
//
//    /**
//     * Returns a function to convert a single {@link Profession} to {@link ProfessionModel}.
//     *
//     * @return new instance
//     */
//    public ProfessionToModelFunction professionToModel() {
//        return new ProfessionToModelFunction(skillToModel());
//    }
//
//

}