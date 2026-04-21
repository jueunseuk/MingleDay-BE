package returns.mingleday.domain.mingle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mingle_log")
public class MingleLog extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mingle_log_id", nullable = false)
    private Long mingleLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_id", nullable = false)
    private Mingle mingle;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "operator_name", nullable = false)
    private String operatorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private TargetType targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "target_name")
    private String targetName;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "mingle_log_type")
    private MingleLogType mingleLogType;

    public static MingleLog of(Mingle mingle, Long operatorId, String operatorName, TargetType targetType, String content, MingleLogType mingleLogType) {
        return MingleLog.builder()
                .mingle(mingle)
                .operatorId(operatorId)
                .operatorName(operatorName)
                .targetType(targetType)
                .content(content)
                .mingleLogType(mingleLogType)
                .build();
    }

    public static MingleLog ofTarget(Mingle mingle, Long operatorId, String operatorName, TargetType targetType, Long targetId, String targetName, String content, MingleLogType mingleLogType) {
        if(targetType == null || targetId == null || targetName == null) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }
        return MingleLog.builder()
                .mingle(mingle)
                .operatorId(operatorId)
                .operatorName(operatorName)
                .targetType(targetType)
                .targetId(targetId)
                .targetName(targetName)
                .content(content)
                .mingleLogType(mingleLogType)
                .build();
    }
}