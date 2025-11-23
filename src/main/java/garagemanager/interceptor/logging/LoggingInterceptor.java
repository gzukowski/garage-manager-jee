package garagemanager.interceptor.logging;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;
import java.util.UUID;
import java.util.logging.Logger;

@LogOperation
@Interceptor
public class LoggingInterceptor {

    private SecurityContext securityContext;

    private final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());

    @Inject
    public LoggingInterceptor(@SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @AroundInvoke
    public Object logOperation(InvocationContext ctx) throws Exception {

        System.out.println("Loguje");
        String username = "anonymous";
        if (securityContext.getCallerPrincipal() != null) {
            username = securityContext.getCallerPrincipal().getName();
        }

        LogOperation annotation = ctx.getMethod().getAnnotation(LogOperation.class);
        String operationName = ctx.getMethod().getName();

        Object[] params = ctx.getParameters();
        String resourceId = null;
        if (params.length > 0) {
            Object param = params[0];
            if (param instanceof UUID) {
                resourceId = param.toString();
            } else {
                try {
                    resourceId = param.getClass().getMethod("getId").invoke(param).toString();
                } catch (Exception ignored) {}
            }
        }

        logger.info(String.format("User: %s, Operation: %s, Resource: %s", username, operationName, resourceId));
        return ctx.proceed();
    }
}
