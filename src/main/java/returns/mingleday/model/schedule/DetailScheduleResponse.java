package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.category.CategoryResponse;

import java.util.List;

@Data
public class DetailScheduleResponse {
    // 특정 날짜의 일정 보는 페이지 응답
    private Long scheduleId;
    private User owner;
    private String title;
    private String content;
    private String location;
    private Boolean isRepeated;
    private Boolean isLocked;
    private Boolean isPrivate;
    private CategoryResponse category;
    private List<ScheduleMemberResponse> members;
    private ScheduleInstanceResponse scheduleInstance;

    public DetailScheduleResponse(Schedule schedule, List<ScheduleMemberResponse> members, ScheduleInstanceResponse scheduleInstance) {
        this.scheduleId = schedule.getScheduleId();
        this.owner = schedule.getOwner();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.location = schedule.getLocation();
        this.isRepeated = schedule.getIsRepeated();
        this.isLocked = schedule.getIsLocked();
        this.isPrivate = schedule.getIsPrivate();
        this.category = new CategoryResponse(schedule.getCategory());
        this.members = members;
        this.scheduleInstance = scheduleInstance;
    }
}
