package garagemanager.carparts.view;

import garagemanager.carparts.model.CarsModel;
import garagemanager.carparts.model.PartsModel;
import garagemanager.carparts.service.CarService;
import garagemanager.carparts.service.PartService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@RequestScoped
@Named
public class PartList {


    private PartService service;
    private PartsModel parts;

    /**
     * Factory producing functions for conversion between models and entities.
     */
    private final ModelFunctionFactory factory;

    @Inject
    public PartList(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    @EJB
    public void setService(PartService service) {
        this.service = service;
    }


    public PartsModel getParts() {
        if (parts == null) {
            parts = factory.partsToModel().apply(service.findAll());
        }
        return parts;
    }

    public String deleteAction(PartsModel.Part part) {
        service.delete(part.getId());
        return "part_list?faces-redirect=true";
    }

}
