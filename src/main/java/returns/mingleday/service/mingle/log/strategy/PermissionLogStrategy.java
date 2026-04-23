package returns.mingleday.service.mingle.log.strategy;

import org.springframework.stereotype.Component;
import returns.mingleday.domain.mingle.*;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.response.code.MingleLogExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Component
public class PermissionLogStrategy implements CreateMingleLogInterface {
    @Override
    public MingleLog create(Mingle mingle, MingleMember operator, Object target) {
        if(!(target instanceof MingleMember mingleMember)) {
            throw new BaseException(MingleLogExceptionCode.INVALID_LOG_TARGET);
        }

        String content = operator.getDisplayName() + "님이 " + mingleMember.getDisplayName() + "님의 권한을 변경했습니다.";

        return MingleLog.ofTarget(
                mingle,
                operator.getMingleMemberId(),
                operator.getDisplayName(),
                TargetType.MEMBER,
                mingleMember.getMingleMemberId(),
                mingleMember.getDisplayName(),
                content,
                MingleLogType.PERMISSION
        );
    }

    @Override
    public MingleLogType getMingleLogType() {
        return MingleLogType.PERMISSION;
    }
}
