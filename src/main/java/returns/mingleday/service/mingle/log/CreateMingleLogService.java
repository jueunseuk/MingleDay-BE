package returns.mingleday.service.mingle.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLog;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.repository.MingleLogRepository;
import returns.mingleday.service.mingle.log.factory.MingleLogFactory;

@Service
@RequiredArgsConstructor
public class CreateMingleLogService {

    private final MingleLogFactory mingleLogFactory;
    private final MingleLogRepository mingleLogRepository;

    @Transactional
    public void execute(Mingle mingle, MingleMember operator, Object target, MingleLogType type) {
        MingleLog log = mingleLogFactory.create(mingle, operator, target, type);
        mingleLogRepository.save(log);
    }
}
