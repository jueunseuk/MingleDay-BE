package returns.mingleday.service.mingle.log.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import returns.mingleday.domain.mingle.*;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.response.code.MingleLogExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Component
@Slf4j
public class CreateLogStrategy implements CreateMingleLogInterface {

    @Override
    public MingleLog create(Mingle mingle, MingleMember operator, Object target) {

        if (target instanceof Schedule schedule) {
            String content = operator.getDisplayName() + "님이 " + schedule.getTitle() + " 일정을 추가했습니다.";

            return MingleLog.ofTarget(
                    mingle,
                    operator.getMingleMemberId(),
                    operator.getDisplayName(),
                    TargetType.SCHEDULE,
                    schedule.getScheduleId(),
                    schedule.getTitle(),
                    content,
                    MingleLogType.CREATE
            );
        }

        if (target instanceof Mingle createdMingle) {
            String content = operator.getDisplayName() + "님이 밍글을 생성했습니다.";

            return MingleLog.ofTarget(
                    createdMingle,
                    operator.getMingleMemberId(),
                    operator.getDisplayName(),
                    TargetType.MINGLE,
                    Long.valueOf(createdMingle.getMingleId()),
                    createdMingle.getName(),
                    content,
                    MingleLogType.CREATE
            );
        }
        log.info("test");
        throw new BaseException(MingleLogExceptionCode.INVALID_LOG_TARGET);
    }

    @Override
    public MingleLogType getMingleLogType() {
        return MingleLogType.CREATE;
    }
}