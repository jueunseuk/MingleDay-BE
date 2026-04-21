package returns.mingleday.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleMemberRequest {
    private Long scheduleMemberId;
    private String memo;
}
