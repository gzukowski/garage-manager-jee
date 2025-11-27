package garagemanager.view.producer;

import garagemanager.view.producer.qualifier.FacesElement;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Creates {@link HttpServletResponse} using injected {@link FacesContext}. The servlet filter is not called for
 * secured JSF views (maybe because of bug or feature) but in JSF based views creation based on context can be used.
 */
public class HttpServletResponseProducer {

    /**
     * Current faces context.
     */
    private final FacesContext facesContext;

    /**
     * @param facesContext current faces context
     */
    @Inject
    public HttpServletResponseProducer(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    /**
     *
     * @return managed HTTP response
     */
    @Produces
    @RequestScoped
    @FacesElement
    HttpServletResponse create() {
        return (HttpServletResponse) facesContext.getExternalContext().getResponse();
    }

}
