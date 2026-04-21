package returns.mingleday.service.mingle.log.strategy;

import org.springframework.stereotype.Component;
import returns.mingleday.domain.mingle.*;

@Component
public class LeaveLogStrategy implements CreateMingleLogInterface {
    @Override
    public MingleLog create(Mingle mingle, MingleMember operator, Object target) {
        String content = operator.getDisplayName() + "님이 밍글을 떠났습니다.";
        return MingleLog.of(
                mingle,
                operator.getMingleMemberId(),
                operator.getDisplayName(),
                TargetType.MINGLE,
                content,
                MingleLogType.LEAVE
        );
    }

    @Override
    public MingleLogType getMingleLogType() {
        return MingleLogType.LEAVE;
    }
}
