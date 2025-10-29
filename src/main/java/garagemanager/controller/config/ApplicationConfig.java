package garagemanager.controller.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Global config for JAX-RS REST services prefix.
 */
@ApplicationPath("/api/v1")//Can be overwritten in web.xml using servlet configuration.
public class ApplicationConfig extends Application {
}

