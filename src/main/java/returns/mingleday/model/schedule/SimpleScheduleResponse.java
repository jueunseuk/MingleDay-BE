package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.category.CategoryResponse;

@Data
public class SimpleScheduleResponse {
    // 주별/월별 일정 보는 페이지 응답 - 리스트로 사용
    private Long scheduleId;
    private User owner;
    private String title;
    private String content;
    private String location;
    private Boolean isRepeated;
    private Boolean isLocked;
    private Boolean isPrivate;
    private CategoryResponse category;
    private SimpleScheduleInstanceResponse scheduleInstance;

    public SimpleScheduleResponse(Schedule schedule, SimpleScheduleInstanceResponse scheduleInstance) {
        this.scheduleId = schedule.getScheduleId();
        this.owner = schedule.getOwner();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.location = schedule.getLocation();
        this.isRepeated = schedule.getIsRepeated();
        this.isLocked = schedule.getIsLocked();
        this.isPrivate = schedule.getIsPrivate();
        this.category = new CategoryResponse(schedule.getCategory());
        this.scheduleInstance = scheduleInstance;
    }

}
