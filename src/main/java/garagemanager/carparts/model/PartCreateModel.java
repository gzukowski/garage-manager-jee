package garagemanager.carparts.model;

import garagemanager.carparts.entity.PartCondition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PartCreateModel {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private LocalDateTime addedDate;
    private PartCondition condition;
    private CarModel car;
    //private User user;
}
