package garagemanager.component;

import garagemanager.carparts.model.function.CarToModelFunction;
import garagemanager.carparts.model.function.CarsToModelFunction;
import garagemanager.carparts.model.function.PartToModelFunction;
import garagemanager.carparts.model.function.PartsToModelFunction;
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

//    /**
//     * Returns a function to convert a single {@link Character} to {@link CharacterEditModel}.
//     *
//     * @return new instance
//     */
//    public CharacterToEditModelFunction characterToEditModel() {
//        return new CharacterToEditModelFunction();
//    }
//
//    /**
//     * Returns a function to convert a single {@link CharacterModel} to {@link Character}.
//     *
//     * @return new instance
//     */
//    public ModelToCharacterFunction modelToCharacter() {
//        return new ModelToCharacterFunction();
//    }
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
//    /**
//     * Returns a function to update a {@link Character}.
//     *
//     * @return UpdateCharacterFunction instance
//     */
//    public UpdateCharacterWithModelFunction updateCharacter() {
//        return new UpdateCharacterWithModelFunction();
//    }

}