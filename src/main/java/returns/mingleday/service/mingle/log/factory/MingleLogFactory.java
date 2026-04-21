package returns.mingleday.service.mingle.log.factory;

import org.springframework.stereotype.Component;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLog;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.service.mingle.log.strategy.CreateMingleLogInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MingleLogFactory {

    private final Map<MingleLogType, CreateMingleLogInterface> strategyMap;

    public MingleLogFactory(List<CreateMingleLogInterface> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(CreateMingleLogInterface::getMingleLogType, s -> s));
    }

    public MingleLog create(Mingle mingle, MingleMember operator, Object target, MingleLogType type) {
        CreateMingleLogInterface strategy = strategyMap.get(type);
        return strategy.create(mingle, operator, target);
    }
}
