package garagemanager.carparts.model;

import garagemanager.carparts.entity.FuelType;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CarModel {

    private UUID id;
    private String name;
    private String brand;
    private int productionYear;
    private FuelType fuelType;
    private double mileage;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Part {
        private UUID id;
        private String name;
        private Long version;
    }

    @Singular
    private List<Part> parts;
}
