package returns.mingleday.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusScheduler {

    private final UserService userService;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void activeToDormant() {
        log.info("[StatisticScheduler] Start detecting inactive users");

        try {
            Integer cnt = userService.userToDormant(1);
            log.info("Finished executing scheduler");
            log.info("Deactivated a total of {} user", cnt);
        } catch (Exception e) {
            log.error("Error running scheduler", e);
        }
    }
}
