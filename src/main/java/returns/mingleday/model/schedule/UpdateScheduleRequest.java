package returns.mingleday.model.schedule;

import lombok.Data;

@Data
public class UpdateScheduleRequest {
    private Long scheduleId;
    private Long scheduleInstanceId;
    private Integer mingleId;
    private String title;
    private String content;
    private String location;
    private Long categoryId;
    private Boolean isLocked;
    private Boolean isPrivate;
}
