package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleMember;

import java.util.List;

@Data
public class MingleMemberResponse {
    private Long memberId;
    private String name;
    private List<MinglesResponse> belongingMingles;

    public MingleMemberResponse(MingleMember mingleMember, List<MinglesResponse> minglesList) {
        this.memberId = mingleMember.getMingleMemberId();
        this.name = mingleMember.getDisplayName();
        this.belongingMingles = minglesList;
    }

}
