package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.schedule.ScheduleStatus;

import java.time.LocalDateTime;

@Data
public class SearchScheduleInstanceResponse {
    // 검색했을 때 보여줄 스케줄 인스턴스 DTO
    private Integer mingleId;
    private Long scheduleId;
    private Long scheduleInstanceId;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String memo;
    private ScheduleStatus scheduleStatus;

    public SearchScheduleInstanceResponse(ScheduleInstance scheduleInstance) {
        this.mingleId = scheduleInstance.getSchedule().getMingle().getMingleId();
        this.scheduleId = scheduleInstance.getSchedule().getScheduleId();
        this.scheduleInstanceId = scheduleInstance.getScheduleInstanceId();
        this.title = scheduleInstance.getSchedule().getTitle();
        this.startAt = scheduleInstance.getStartAt();
        this.endAt = scheduleInstance.getEndAt();
        this.memo = scheduleInstance.getMemo();
        this.scheduleStatus = scheduleInstance.getScheduleStatus();
    }
}
