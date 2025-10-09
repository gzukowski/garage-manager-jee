package garagemanager.configuration.listener;

import garagemanager.component.DtoFunctionFactory;
import garagemanager.user.controller.impl.UserControllerImpl;
import garagemanager.user.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of controllers and puts them in
 * the application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateControllers implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        UserService characterService = (UserService) event.getServletContext().getAttribute("userService");

        event.getServletContext().setAttribute("userController", new UserControllerImpl(
                characterService,
                new DtoFunctionFactory()
        ));

    }
}

