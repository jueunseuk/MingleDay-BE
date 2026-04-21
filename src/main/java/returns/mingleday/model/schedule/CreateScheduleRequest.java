package returns.mingleday.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import returns.mingleday.domain.schedule.EndType;
import returns.mingleday.domain.schedule.RepeatType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateScheduleRequest {
    private Integer mingleId;
    private Integer userId;
    private String title;
    private String content;
    private String location;
    private Long categoryId;
    private Boolean isRepeated;
    private Boolean isLocked;
    private Boolean isPrivate;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private List<ScheduleMemberRequest> mingleMembers;

    private RepeatType repeatType;
    private String repeatValue;
    private EndType endType;
    private String endValue;
}
