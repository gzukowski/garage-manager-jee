package garagemanager.carparts.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PartsModel {

    @Singular
    private List<Part> parts;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Part {
        private java.util.UUID id;
        private String name;
        private String carName;
        private double price;
        private String condition;
    }
}
