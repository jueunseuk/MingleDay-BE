package returns.mingleday.flow.authentication.withdraw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.EmailRepository;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WithdrawFlow {

    private final UserService userService;
    private final EmailRepository emailRepository;

    @Transactional
    public void withdraw(Integer userId) {
        User user = userService.findUserByUserId(userId);

        log.info("Deleted Email and User: userId: {}, email: {}", userId, user.getEmail());
        emailRepository.deleteEmailByEmail(user.getEmail());
        user.withdraw();
    }
}
