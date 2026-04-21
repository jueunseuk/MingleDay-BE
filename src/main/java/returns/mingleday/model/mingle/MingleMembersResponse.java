package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;

@Data
public class MingleMembersResponse {
    private Integer userId;
    private Long memberId;
    private String name;
    private String profileUrl;

    public MingleMembersResponse(User user, MingleMember mingleMember) {
        this.userId = user.getUserId();
        this.memberId = mingleMember.getMingleMemberId();
        this.name = mingleMember.getDisplayName();
        this.profileUrl = user.getProfileUrl();
    }
}
