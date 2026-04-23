package returns.mingleday.model.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import returns.mingleday.domain.user.Purpose;

@Data
@AllArgsConstructor
public class EmailCodeRequest {
    private String email;
    private Purpose purpose;
}
