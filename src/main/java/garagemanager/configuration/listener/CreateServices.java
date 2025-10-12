package garagemanager.configuration.listener;

import garagemanager.crypto.component.Pbkdf2PasswordHash;
import garagemanager.datastorage.component.DataStorage;
import garagemanager.user.repository.api.UserRepository;

import garagemanager.user.repository.inmemory.UserInMemRepository;
import garagemanager.user.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of services (business layer) and
 * puts them in the application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateServices implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataStorage dataSource = (DataStorage) event.getServletContext().getAttribute("dataStorage");

        UserRepository userRepository = new UserInMemRepository(dataSource);

        event.getServletContext().setAttribute("userService", new UserService(userRepository, new Pbkdf2PasswordHash(), ""));
    }

}

