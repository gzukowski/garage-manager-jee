package garagemanager.carparts.model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        private java.util.UUID id;
        private String name;
        private String carName;
        private double price;
        private String condition;

        private Long version;
        private LocalDateTime creationDateTime;
        private LocalDateTime lastModifiedDateTime;
        
        public String getFormattedCreationDateTime() {
            return creationDateTime != null ? creationDateTime.format(FORMATTER) : "-";
        }
        
        public String getFormattedLastModifiedDateTime() {
            return lastModifiedDateTime != null ? lastModifiedDateTime.format(FORMATTER) : "-";
        }
    }
}
