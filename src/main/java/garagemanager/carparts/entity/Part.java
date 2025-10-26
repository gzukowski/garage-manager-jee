package garagemanager.carparts.entity;

import garagemanager.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Part implements Serializable {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private LocalDateTime addedDate;
    private PartCondition condition;
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    private Car car;
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    private User user;
}
