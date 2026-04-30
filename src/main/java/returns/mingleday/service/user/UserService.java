package returns.mingleday.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.user.UpdateProfileInfoRequest;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String name, String email, String password, String nickname) {
        User.isValidPassword(password);
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.of(
                name, email, encodedPassword, nickname == null || nickname.isEmpty() ? name : nickname
        );

        return userRepository.save(user);
    }

    public User findUserByUserId(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(UserExceptionCode.NOT_EXIST_USER));
    }

    @Transactional
    public void updateProfileInfo(Integer userId, UpdateProfileInfoRequest request) {
        User user = findUserByUserId(userId);
        user.updateProfileInfo(request.getName(), request.getNickname());
        log.info("Success to update profile info - userId : {}", userId);
    }

    public Integer userToDormant(Integer month) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(month);
        List<User> candidates = userRepository.findAllByLastLoginAtLessThan(monthAgo);

        for(User user : candidates) {
            user.toDormant();
        }

        return candidates.size();
    }
}
