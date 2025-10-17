package garagemanager.configuration.observer;


import garagemanager.user.entity.User;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.SneakyThrows;
import lombok.ToString;


import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@ApplicationScoped
public class InitializedData {

    /**
     * User service.
     */
    private UserService userService;

    private final RequestContextController requestContextController;
    @Inject
    public InitializedData(
           // CharacterService characterService,
            UserService userService,
            //ProfessionService professionService,
            RequestContextController requestContextController
    ) {
       // this.characterService = characterService;
        this.userService = userService;
        //this.professionService = professionService;
        this.requestContextController = requestContextController;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @SneakyThrows
    private void init() {

        requestContextController.activate();

        createUserWithPhoto(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"),
                "admin", "System", "Admin", LocalDate.of(1990, 10, 21),
                "admin@simplerpg.example.com", "adminadmin",
                List.of(UserRoles.ADMIN, UserRoles.USER),
                Path.of("C:\\Users\\gzukowski\\IdeaProjects\\GarageManager\\src\\main\\resources\\garagemanager\\configuration\\photo\\admin.png"));

        createUserWithPhoto(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"),
                "kevin", "Kevin", "Pear", LocalDate.of(2001, 1, 16),
                "kevin@example.com", "useruser",
                List.of(UserRoles.USER),
                Path.of("C:\\Users\\gzukowski\\IdeaProjects\\GarageManager\\src\\main\\resources\\garagemanager\\configuration\\photo\\Kevin.png"));

        createUserWithPhoto(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"),
                "alice", "Alice", "Grape", LocalDate.of(2002, 3, 19),
                "alice@example.com", "useruser",
                List.of(UserRoles.USER),
                Path.of("C:\\Users\\gzukowski\\IdeaProjects\\GarageManager\\src\\main\\resources\\garagemanager\\configuration\\photo\\Alice.png"));
    }

    @SneakyThrows
    private void createUserWithPhoto(UUID id, String login, String name, String surname,
                                     LocalDate birthDate, String email, String password,
                                     List<String> roles, Path photoPath) {

        User user = User.builder()
                .id(id)
                .login(login)
                .name(name)
                .surname(surname)
                .birthDate(birthDate)
                .email(email)
                .password(password)
                .roles(roles)
                .photoPath(String.valueOf(photoPath))
                .build();

        userService.create(user);

    }

}
