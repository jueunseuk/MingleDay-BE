package returns.mingleday.service.mingle.log.strategy;

import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLog;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;

public interface CreateMingleLogInterface {
    MingleLog create(Mingle mingle, MingleMember mingleMember, Object target);
    MingleLogType getMingleLogType();
}
