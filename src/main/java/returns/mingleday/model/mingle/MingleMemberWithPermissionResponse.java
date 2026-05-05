package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleMember;

import java.util.List;

@Data
public class MingleMemberWithPermissionResponse {
    private Long memberId;
    private String name;
    private List<MinglePermissionResponse> permissions;

    public MingleMemberWithPermissionResponse(MingleMember mingleMember, List<MinglePermissionResponse> permissions) {
        this.memberId = mingleMember.getMingleMemberId();
        this.name = mingleMember.getDisplayName();
        this.permissions = permissions;
    }
}
