package manage.store.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import manage.store.model.user.value.UserId;

@Data
@AllArgsConstructor
public class LoginResponse {
    private UserId id;
    private String token;
}
