package garagemanager.configuration.observer;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.FuelType;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.entity.PartCondition;
import garagemanager.carparts.service.CarService;
import garagemanager.carparts.service.PartService;
import garagemanager.user.entity.User;
import garagemanager.user.entity.UserRoles;
import garagemanager.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class InitializedData {

    private final UserService userService;
    private final PartService partService;
    private final CarService carService;
    private final RequestContextController requestContextController;

    @Inject
    public InitializedData(
            CarService carService,
            UserService userService,
            PartService partService,
            RequestContextController requestContextController
    ) {
        this.carService = carService;
        this.userService = userService;
        this.partService = partService;
        this.requestContextController = requestContextController;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    @SneakyThrows
    private void init() {

        requestContextController.activate();

        // === Tworzenie użytkowników ===
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
                .id(UUID.randomUUID())
                .name("A4")
                .brand("Audi")
                .productionYear(2004)
                .fuelType(FuelType.DIESEL)
                .mileage(120_000)
                .build();
        carService.create(audi);

        Car bmw = Car.builder()
                .id(UUID.randomUUID())
                .name("320d")
                .brand("BMW")
                .productionYear(2019)
                .fuelType(FuelType.DIESEL)
                .mileage(98_000)
                .build();
        carService.create(bmw);

        Car opel = Car.builder()
                .id(UUID.randomUUID())
                .name("Astra H")
                .brand("Opel")
                .productionYear(2005)
                .fuelType(FuelType.DIESEL)
                .mileage(230_000)
                .build();
        carService.create(opel);

        Part oilFilter = Part.builder()
                .id(UUID.randomUUID())
                .name("Filtr oleju")
                .description("Filtr oleju do silnika 2.0 TDI")
                .price(49.99)
                .addedDate(LocalDateTime.now())
                .condition(PartCondition.NEW)
                .car(audi)
                .user(kevin)
                .build();
        partService.create(oilFilter, audi, kevin);

        Part tires = Part.builder()
                .id(UUID.randomUUID())
                .name("Opony zimowe")
                .description("Zestaw 4 opon zimowych 225/45R17")
                .price(1800.00)
                .addedDate(LocalDateTime.now())
                .condition(PartCondition.USED)
                .car(bmw)
                .user(alice)
                .build();
        partService.create(tires, bmw, alice);

        Part grill = Part.builder()
                .id(UUID.randomUUID())
                .name("Grill OPC")
                .description("Grill astra OPC zamiennik")
                .price(1200.00)
                .addedDate(LocalDateTime.now())
                .condition(PartCondition.NEW)
                .car(opel)
                .user(admin)
                .build();
        partService.create(grill, opel, admin);

        System.out.println("=== Uzytkownicy ===");
        userService.findAll().forEach(user ->
                System.out.printf("ID: %s | Login: %s | Email: %s%n",
                        user.getId(), user.getLogin(), user.getEmail())
        );

        System.out.println("\n=== Samochody ===");
        carService.findAll().forEach(car ->
                System.out.printf("ID: %s | Brand: %s | Name: %s | Year: %d%n",
                        car.getId(), car.getBrand(), car.getName(), car.getProductionYear())
        );

        System.out.println("\n=== Czesci ===");
        partService.findAll().forEach(part -> {
            String owner = part.getUser() != null ? part.getUser().getLogin() : "Brak";
            String car = part.getCar() != null ? part.getCar().getName() : "Brak";
            System.out.printf("ID: %s | Nazwa: %s | Wlasciciel: %s | Samochod: %s%n",
                    part.getId(), part.getName(), owner, car);
        });

        System.out.println("\nUsuwanie części: " + tires.getName());
        partService.delete(tires.getId());

        System.out.println("\nCzesci po usunieciu:");
        partService.findAll().forEach(part -> {
            String owner = part.getUser() != null ? part.getUser().getLogin() : "Brak";
            String car = part.getCar() != null ? part.getCar().getName() : "Brak";
            System.out.printf("ID: %s | Nazwa: %s | Wlasciciel: %s | Samochod: %s%n",
                    part.getId(), part.getName(), owner, car);
        });


        requestContextController.deactivate();
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
