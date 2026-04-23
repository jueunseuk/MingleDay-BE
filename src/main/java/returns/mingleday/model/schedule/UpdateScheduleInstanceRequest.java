package returns.mingleday.model.schedule;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateScheduleInstanceRequest {
    private Long scheduleInstanceId;
    private Long scheduleId;
    private Integer mingleId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String memo;
}
