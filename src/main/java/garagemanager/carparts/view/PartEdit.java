package garagemanager.carparts.view;

import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartEditModel;
import garagemanager.carparts.service.PartService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;


@ViewScoped
@Named
public class PartEdit implements Serializable {


    private PartService service;

    private final ModelFunctionFactory factory;


    @Setter
    @Getter
    private UUID id;

    @Getter
    private PartEditModel part;


    @Inject
    public PartEdit(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    @EJB
    public void setService(PartService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached within
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Part> part = service.find(id);
        if (part.isPresent()) {
            this.part = factory.partToEditModel().apply(part.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Part not found");
        }
    }

    /**
     * Action initiated by clicking save button.
     *
     * @return navigation case to the same page
     */
    public String saveAction() {
        service.update(factory.updatePart().apply(service.find(id).orElseThrow(), part));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }

}
