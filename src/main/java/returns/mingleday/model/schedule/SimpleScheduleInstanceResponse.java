package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.ScheduleInstance;

import java.time.LocalDateTime;

@Data
public class SimpleScheduleInstanceResponse {
    // ScheduleInstanceResponse에서 prev와 next 용도로 보여줄 값
    private Long scheduleInstanceId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public SimpleScheduleInstanceResponse(ScheduleInstance scheduleInstance) {
        if (scheduleInstance == null) {
            return;
        }
        this.scheduleInstanceId = scheduleInstance.getScheduleInstanceId();
        this.startAt = scheduleInstance.getStartAt();
        this.endAt = scheduleInstance.getEndAt();
    }
}
