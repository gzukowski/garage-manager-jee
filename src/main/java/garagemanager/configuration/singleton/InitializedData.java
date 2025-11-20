package garagemanager.configuration.singleton;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.FuelType;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.entity.PartCondition;
import garagemanager.carparts.service.CarService;
import garagemanager.carparts.service.PartService;
import garagemanager.user.entity.User;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * EJB singleton can be forced to start automatically when application starts. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@NoArgsConstructor
@DependsOn("InitializeAdminService")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
@RunAs(UserRoles.ADMIN)
@Log
public class InitializedData {

    private PartService partService;

    private UserService userService;

    private CarService carService;

    @EJB
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @EJB
    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @PostConstruct
    @SneakyThrows
    private void init() {
        System.out.println("Init initialized");

        if (userService.initFind("admin").isEmpty()) {
            System.out.println("NIE MA ADMINA");
            User admin = createUserWithPhoto(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"),
                    "admin", "System", "Admin", LocalDate.of(1990, 10, 21),
                    "admin@simplerpg.example.com", "adminadmin",
                    List.of(UserRoles.ADMIN, UserRoles.USER),
                    Path.of("C:\\Users\\gzukowski\\IdeaProjects\\GarageManager\\src\\main\\resources\\garagemanager\\configuration\\photo\\admin.png"));

            User kevin = createUserWithPhoto(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"),
                    "kevin", "Kevin", "Pear", LocalDate.of(2001, 1, 16),
                    "kevin@example.com", "useruser",
                    List.of(UserRoles.USER),
                    Path.of("C:\\Users\\gzukowski\\IdeaProjects\\GarageManager\\src\\main\\resources\\garagemanager\\configuration\\photo\\Kevin.png"));

            User alice = createUserWithPhoto(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"),
                    "alice", "Alice", "Grape", LocalDate.of(2002, 3, 19),
                    "alice@example.com", "useruser",
                    List.of(UserRoles.USER),
                    Path.of("C:\\Users\\gzukowski\\IdeaProjects\\GarageManager\\src\\main\\resources\\garagemanager\\configuration\\photo\\Alice.png"));

            Car audi = Car.builder()
                    .id(UUID.fromString("3c6f1eb1-8069-44ab-988f-d7fed2b65d87"))
                    .name("A4")
                    .brand("Audi")
                    .productionYear(2004)
                    .fuelType(FuelType.DIESEL)
                    .mileage(120_000)
                    .build();

            Car bmw = Car.builder()
                    .id(UUID.fromString("ab5700af-a130-4598-bb3d-e16233b40aa1"))
                    .name("320d")
                    .brand("BMW")
                    .productionYear(2019)
                    .fuelType(FuelType.DIESEL)
                    .mileage(98_000)
                    .build();

            Car opel = Car.builder()
                    .id(UUID.fromString("4e66cd84-25e6-4a53-ad16-0e1de1ace74e"))
                    .name("Astra H")
                    .brand("Opel")
                    .productionYear(2005)
                    .fuelType(FuelType.DIESEL)
                    .mileage(230_000)
                    .build();

            carService.initCreate(audi);
            carService.initCreate(bmw);
            carService.initCreate(opel);

            Part oilFilter = Part.builder()
                    .id(UUID.fromString("a842ffcf-5dc7-43ca-831f-cb0c89479087"))
                    .name("Filtr oleju")
                    .description("Filtr oleju do silnika 2.0 TDI")
                    .price(49.99)
                    .addedDate(LocalDateTime.now())
                    .condition(PartCondition.NEW)
                    .car(audi)
                    .user(kevin)
                    .build();


            Part tires = Part.builder()
                    .id(UUID.fromString("3e039122-8ace-43fb-b41e-484f63ee2af2"))
                    .name("Opony zimowe")
                    .description("Zestaw 4 opon zimowych 225/45R17")
                    .price(1800.00)
                    .addedDate(LocalDateTime.now())
                    .condition(PartCondition.USED)
                    .car(bmw)
                    .user(alice)
                    .build();

            Part grill = Part.builder()
                    .id(UUID.fromString("66418204-5720-4144-98e3-44cdd1c5cfd0"))
                    .name("Grill OPC")
                    .description("Grill astra OPC zamiennik")
                    .price(1200.00)
                    .addedDate(LocalDateTime.now())
                    .condition(PartCondition.NEW)
                    .car(opel)
                    .user(admin)
                    .build();

            partService.initCreate(oilFilter);
            partService.initCreate(grill);
            partService.initCreate(tires);
        }
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

    @SneakyThrows
    private User createUserWithPhoto(UUID id, String login, String name, String surname,
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
        return user;
    }

}
