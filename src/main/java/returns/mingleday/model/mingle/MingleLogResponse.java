package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleLog;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.TargetType;

import java.time.LocalDateTime;

@Data
public class MingleLogResponse {
    private Long mingleLogId;
    private Integer mingleId;
    private Long operatorId;
    private String operatorName;
    private TargetType targetType;
    private Long targetId;
    private String targetName;
    private String content;
    private MingleLogType mingleLogType;
    private LocalDateTime createdAt;

    public MingleLogResponse(MingleLog mingleLog) {
        this.mingleLogId = mingleLog.getMingleLogId();
        this.mingleId = mingleLog.getMingle().getMingleId();
        this.operatorId = mingleLog.getOperatorId();
        this.operatorName = mingleLog.getOperatorName();
        this.targetType = mingleLog.getTargetType();
        this.targetId = mingleLog.getTargetId();
        this.targetName = mingleLog.getTargetName();
        this.content = mingleLog.getContent();
        this.mingleLogType = mingleLog.getMingleLogType();
        this.createdAt = mingleLog.getCreatedAt();
    }
}
