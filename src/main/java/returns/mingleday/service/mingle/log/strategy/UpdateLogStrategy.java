package returns.mingleday.service.mingle.log.strategy;

import org.springframework.stereotype.Component;
import returns.mingleday.domain.mingle.*;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.response.code.MingleLogExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Component
public class UpdateLogStrategy implements CreateMingleLogInterface {

    @Override
    public MingleLog create(Mingle mingle, MingleMember operator, Object target) {
        if (target instanceof Schedule schedule) {
            String content = operator.getDisplayName() + "님이 "
                    + schedule.getTitle() + " 일정을 수정했습니다.";

            return MingleLog.ofTarget(
                    mingle,
                    operator.getMingleMemberId(),
                    operator.getDisplayName(),
                    TargetType.SCHEDULE,
                    schedule.getScheduleId(),
                    schedule.getTitle(),
                    content,
                    MingleLogType.MODIFY
            );
        }

        if (target instanceof Mingle updatedMingle) {
            String content = operator.getDisplayName() + "님이 밍글 정보를 수정했습니다.";

            return MingleLog.ofTarget(
                    updatedMingle,
                    operator.getMingleMemberId(),
                    operator.getDisplayName(),
                    TargetType.MINGLE,
                    Long.valueOf(updatedMingle.getMingleId()),
                    updatedMingle.getName(),
                    content,
                    MingleLogType.MODIFY
            );
        }

        throw new BaseException(MingleLogExceptionCode.INVALID_LOG_TARGET);
    }

    @Override
    public MingleLogType getMingleLogType() {
        return MingleLogType.MODIFY;
    }
}