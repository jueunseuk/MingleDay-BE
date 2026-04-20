package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleInvitation;
import returns.mingleday.domain.mingle.ResponseType;

import java.time.LocalDateTime;

@Data
public class MingleInvitationResponse {
    private Long mingleInvitationId;
    private String mingleName;
    private String mingleMemberName;
    private LocalDateTime expiredAt;
    private LocalDateTime invitedAt;
    private ResponseType responseType;
    private LocalDateTime updatedAt;

    public MingleInvitationResponse(MingleInvitation mingleInvitation) {
        this.mingleInvitationId = mingleInvitation.getMingleInvitationId();
        this.mingleName = mingleInvitation.getMingle().getName();
        this.mingleMemberName = mingleInvitation.getMingleMember().getUser().getName();
        this.expiredAt = mingleInvitation.getExpiredAt();
        this.invitedAt = mingleInvitation.getCreatedAt();
        this.updatedAt = mingleInvitation.getUpdatedAt();
        this.responseType = mingleInvitation.getResponseType();
    }
}
