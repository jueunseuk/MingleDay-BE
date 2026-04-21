package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleLog;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.TargetType;

import java.time.LocalDateTime;

@Data
public class MyMingleLogResponse {
    private Long mingleLogId;
    private MingleResponse mingleResponse;
    private Long operatorId;
    private String operatorName;
    private TargetType targetType;
    private Long targetId;
    private String targetName;
    private String content;
    private MingleLogType mingleLogType;
    private LocalDateTime createdAt;

    public MyMingleLogResponse(MingleLog mingleLog) {
        this.mingleLogId = mingleLog.getMingleLogId();
        this.mingleResponse = new MingleResponse(mingleLog.getMingle());
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
