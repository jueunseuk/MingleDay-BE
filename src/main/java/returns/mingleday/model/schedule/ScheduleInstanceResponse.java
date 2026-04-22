package returns.mingleday.model.schedule;

import lombok.Data;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.schedule.ScheduleStatus;

import java.time.LocalDateTime;

@Data
public class ScheduleInstanceResponse {
    // 특정 날짜의 일정 인스턴스를 볼 때 보여줄 자세한 값
    // 스케줄과 함께 항상 하단에서 보여줌
    private Long scheduleInstanceId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String memo;
    private ScheduleStatus scheduleStatus;
    private SimpleScheduleInstanceResponse prev;
    private SimpleScheduleInstanceResponse next;

    public ScheduleInstanceResponse(ScheduleInstance scheduleInstance, SimpleScheduleInstanceResponse prev, SimpleScheduleInstanceResponse next) {
        this.scheduleInstanceId = scheduleInstance.getScheduleInstanceId();
        this.startAt = scheduleInstance.getStartAt();
        this.endAt = scheduleInstance.getEndAt();
        this.memo = scheduleInstance.getMemo();
        this.scheduleStatus = scheduleInstance.getScheduleStatus();
        this.prev = prev;
        this.next = next;
    }
}
