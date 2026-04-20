package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import returns.mingleday.repository.MingleLogRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MingleLogService {

    private final MingleLogRepository mingleLogRepository;


}
