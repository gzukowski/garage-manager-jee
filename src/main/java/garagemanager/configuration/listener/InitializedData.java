package garagemanager.configuration.listener;


import garagemanager.user.entity.User;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.SneakyThrows;
import lombok.ToString;


import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@WebListener//using annotation does not allow configuring order
public class InitializedData implements ServletContextListener {

    /**
     * User service.
     */
    private UserService userService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        userService = (UserService) event.getServletContext().getAttribute("userService");
        init();
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @SneakyThrows
    private void init() {
        User admin = User.builder()
                .id(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"))
                .name("System")
                .surname("Admin")
                .birthDate(LocalDate.of(1990, 10, 21))
                .login("admin")
                .email("admin@simplerpg.example.com")
                .password("adminadmin")
                .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                .build();

        User kevin = User.builder()
                .id(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"))
                .name("Kevin")
                .surname("Pear")
                .birthDate(LocalDate.of(2001, 1, 16))
                .login("kevin")
                .email("kevin@example.com")
                .password("useruser")
                .roles(List.of(UserRoles.USER))
                .build();

        User alice = User.builder()
                .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"))
                .name("Alice")
                .surname("Grape")
                .birthDate(LocalDate.of(2002, 3, 19))
                .login("alice")
                .email("alice@example.com")
                .password("useruser")
                .roles(List.of(UserRoles.USER))
                .build();

        userService.create(admin);
        userService.create(kevin);
        userService.create(alice);
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            if (is != null) {
                return is.readAllBytes();
            } else {
                throw new IllegalStateException("Unable to get resource %s".formatted(name));
            }
        }
    }

}
