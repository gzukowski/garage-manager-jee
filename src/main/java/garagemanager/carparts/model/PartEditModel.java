package garagemanager.carparts.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PartEditModel {

    private UUID id;
    private String name;
    private double price;
    private String description;
}
