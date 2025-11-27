package garagemanager.user.view;

import garagemanager.view.producer.qualifier.FacesElement;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

/**
 * View bean for handling form login.
 */
@RequestScoped
@Named
@Log
public class UserLogin {

    /**
     * Current HTTP request.
     *
     *
     *
     */
    private final HttpServletRequest request;

    private final HttpServletResponse response;

    /**
     * Security context.
     */
    private final SecurityContext securityContext;

    /**
     * Faces context.
     */
    private final FacesContext facesContext;

    /**
     * @param request         current HTTP request
     * @param response
     * @param securityContext security context
     * @param facesContext    faces context
     */
    @Inject
    public UserLogin(
            HttpServletRequest request,
            @FacesElement HttpServletResponse response,
            @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext,
            FacesContext facesContext
    ) {
        this.request = request;
        this.response = response;
        this.securityContext = securityContext;
        this.facesContext = facesContext;
    }

    /**
     * View model, username.
     */
    @Getter
    @Setter
    private String login;

    /**
     * VIew model, password.
     */
    @Getter
    @Setter
    private String password;

    /**
     * Action initiated by clicking login button.
     */
    @SneakyThrows
    public void loginAction() {
        Credential credential = new UsernamePasswordCredential(login, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(request, response, withParams().credential(credential));
        facesContext.responseComplete();
    }

}
