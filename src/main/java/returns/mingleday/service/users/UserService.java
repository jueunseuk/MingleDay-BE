package returns.mingleday.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.users.User;
import returns.mingleday.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String name, String email, String password, String nickname) {
        User.isValidPassword(password);
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.of(
                name, email, encodedPassword, nickname
        );

        return userRepository.save(user);
    }

}
