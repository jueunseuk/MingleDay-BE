package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.ResponseType;

@Data
public class ResponseMingleRequest {
    private Long mingleInvitationId;
    private ResponseType responseType;
}
