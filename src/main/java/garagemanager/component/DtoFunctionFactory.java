package garagemanager.component;

import garagemanager.carparts.dto.function.*;
import garagemanager.user.dto.function.*;

public class DtoFunctionFactory {

    public PartToResponseFunction partToResponse() {
        return new PartToResponseFunction();
    }

    public PartsToResponseFunction partsToResponse() {
        return new PartsToResponseFunction();
    }

    public CarToResponseFunction carToResponse() {
        return new CarToResponseFunction();
    }

    public CarsToResponseFunction carsToResponse() {
        return new CarsToResponseFunction();
    }

    public RequestToPartFunction requestToPart() {
        return new RequestToPartFunction();
    }

    public RequestUpdatePartFunction updatePart() {
        return new RequestUpdatePartFunction();
    }

    public RequestToUserFunction requestToUser() {
        return new RequestToUserFunction();
    }

    public RequestToUserUpdateFunction updateUser() {
        return new RequestToUserUpdateFunction();
    }

    public RequestToPasswordUpdateFunction updateUserPassword() {
        return new RequestToPasswordUpdateFunction();
    }

    public UsersToResponseFunction usersToResponse() {
        return new UsersToResponseFunction();
    }

    public UserToResponseFunction userToResponse() {
        return new UserToResponseFunction();
    }


}
