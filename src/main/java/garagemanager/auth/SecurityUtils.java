package garagemanager.auth;

import garagemanager.user.entity.UserRoles;
import jakarta.ejb.EJBAccessException;
import jakarta.security.enterprise.SecurityContext;

import java.util.Optional;
import java.util.function.Function;

public class SecurityUtils {

    private SecurityUtils() {}

    public static <T> void checkOwnership(
            Optional<T> entity,
            Function<T, String> ownerGetter,
            SecurityContext securityContext) throws EJBAccessException {

        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return;
        }

        if (securityContext.isCallerInRole(UserRoles.USER)
                && entity.isPresent()
                && ownerGetter.apply(entity.get())
                .equals(securityContext.getCallerPrincipal().getName())) {
            return;
        }

        throw new EJBAccessException("Caller not authorized.");
    }
}
