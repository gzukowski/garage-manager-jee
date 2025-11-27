package garagemanager.carparts.model;

import garagemanager.carparts.entity.PartCondition;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PartModel {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private LocalDateTime addedDate;
    private PartCondition condition;

    private Long version;
    private LocalDateTime creationDateTime;

    private UUID carId;
    private String carName;
    private String carBrand;

    private UUID userId;
    private String userName;
}
