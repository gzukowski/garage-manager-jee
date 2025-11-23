package garagemanager.carparts.view;

import garagemanager.carparts.entity.Part;
import garagemanager.carparts.model.PartModel;
import garagemanager.carparts.service.PartService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
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

/**
 * View bean for rendering single character information.
 */
@ViewScoped
@Named
public class PartView implements Serializable {

    private PartService service;
    private final ModelFunctionFactory factory;

    /**
     * Part id.
     */
    @Setter
    @Getter
    private UUID id;


    @Getter
    private PartModel part;


    @Inject
    public PartView (ModelFunctionFactory factory) {
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

        try {
            Optional<Part> part = service.find(id);
            if (part.isPresent()) {
                this.part = factory.partToModel().apply(part.get());
            } else {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Part not found");
            }
        } catch (EJBAccessException e) {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }

    }

}
