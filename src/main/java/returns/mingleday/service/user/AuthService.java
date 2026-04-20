package returns.mingleday.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    public void logout(Integer userId) {
        log.info("Logout - userId: {}", userId);
    }
}
