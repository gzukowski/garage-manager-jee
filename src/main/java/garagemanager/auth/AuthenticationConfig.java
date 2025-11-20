package garagemanager.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 * Configuration class for security context. There are three authentication mechanism and only one can be used at a
 * time:
 * <ul>
 *     <li>{@link BasicAuthenticationMechanismDefinition} every secured resource is secured with basic auth mechanism,
 *     ideal for REST services (JAX-RS and Servlet). No login forms work but rest services can be used from clients.</li>
 *     <li>{@link FormAuthenticationMechanismDefinition} every secured resource is secured with form auth mechanism,
 *     ideal for HTML web pages (JSF). REST services can not be called from clients as form auth is required.</li>
 *     <li>{@link CustomFormAuthenticationMechanismDefinition} every secured resource is secured with form auth mechanism,
 *     auth form can used custom backend bean method.</li>
 * </ul>
 * Both form based methods required {@link LoginToContinue} configuration.
 */
@ApplicationScoped
//@BasicAuthenticationMechanismDefinition(realmName = "Garage manager")
//@FormAuthenticationMechanismDefinition(
//        loginToContinue = @LoginToContinue(
//                loginPage = "/auth/form/login.xhtml",
//                errorPage = "/auth/form/login_error.xhtml"
//        )
//)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/auth/custom/login.xhtml",
                errorPage = "/auth/custom/login_error.xhtml"
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/GarageManagerParts",
        callerQuery = "select password from users where login = ?",
        groupsQuery = "select role from users__roles where id = (select id from users where login = ?)",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class AuthenticationConfig {
}