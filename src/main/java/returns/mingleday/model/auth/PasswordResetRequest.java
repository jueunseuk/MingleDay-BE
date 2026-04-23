package returns.mingleday.model.auth;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String password;
}
