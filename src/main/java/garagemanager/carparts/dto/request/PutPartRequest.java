package garagemanager.carparts.dto.request;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.PartCondition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PutPartRequest {
    private String name;
    private String description;
    private double price;
    private PartCondition condition;
    private Car car;
}


