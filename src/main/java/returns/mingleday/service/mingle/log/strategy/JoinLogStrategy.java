package returns.mingleday.service.mingle.log.strategy;

import org.springframework.stereotype.Component;
import returns.mingleday.domain.mingle.*;

@Component
public class JoinLogStrategy implements CreateMingleLogInterface {
    @Override
    public MingleLog create(Mingle mingle, MingleMember operator, Object target) {
        String content = operator.getDisplayName() + "님이 밍글에 참여하셨습니다.";
        return MingleLog.of(
                mingle,
                operator.getMingleMemberId(),
                operator.getDisplayName(),
                TargetType.NONE,
                content,
                MingleLogType.JOIN
        );
    }

    @Override
    public MingleLogType getMingleLogType() {
        return MingleLogType.JOIN;
    }
}
