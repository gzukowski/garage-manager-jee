package garagemanager.carparts.dto.response;

import garagemanager.carparts.entity.FuelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetCarResponse {
    private UUID id;
    private String name;
    private String brand;
    private int productionYear;
    private double mileage;
    private FuelType fuelType;
}
