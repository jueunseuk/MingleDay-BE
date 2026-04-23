package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.model.category.SimpleCategoryResponse;

@Data
public class DailyScheduleResponse {
    // 리스트 형태로 하루 치의 일정을 보여주는 DTO
    private Long scheduleId;
    private String title;
    private String content;
    private Boolean isRepeated;
    private Boolean isPrivate;
    private SimpleCategoryResponse category;
    private SimpleScheduleInstanceResponse scheduleInstance;

    public DailyScheduleResponse(Schedule schedule, SimpleScheduleInstanceResponse scheduleInstance) {
        this.scheduleId = schedule.getScheduleId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.isRepeated = schedule.getIsRepeated();
        this.isPrivate = schedule.getIsPrivate();
        this.category = new SimpleCategoryResponse(schedule.getCategory());
        this.scheduleInstance = scheduleInstance;
    }
}
