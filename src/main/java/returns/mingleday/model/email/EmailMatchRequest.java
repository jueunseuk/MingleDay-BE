package returns.mingleday.model.email;

import lombok.Data;
import returns.mingleday.domain.user.Purpose;

@Data
public class EmailMatchRequest {
    private String email;
    private String code;
    private Purpose purpose;
}
