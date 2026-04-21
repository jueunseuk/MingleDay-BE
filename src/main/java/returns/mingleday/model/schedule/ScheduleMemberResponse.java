package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.ScheduleMember;

@Data
public class ScheduleMemberResponse {
    // 특정 날짜의 일정 인스턴스에서 해당되는 멤버 - 리스트 or 단일
    private Long scheduleMemberId;
    private String name;
    private String memo;

    public ScheduleMemberResponse(ScheduleMember scheduleMember) {
        this.scheduleMemberId = scheduleMember.getScheduleMemberId();
        this.name = scheduleMember.getMingleMember().getDisplayName();
        this.memo = scheduleMember.getMemo();
    }
}
