package garagemanager.user.dto.response;

import garagemanager.user.entity.UserRoles;

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
public class GetUserResponse {
    private UUID id;
    private String login;
    private String name;
    private String surname;
    private String email;
    private UserRoles role;
}
