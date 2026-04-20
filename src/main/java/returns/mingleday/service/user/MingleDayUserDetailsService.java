package returns.mingleday.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.user.Status;
import returns.mingleday.domain.user.User;
import returns.mingleday.global.authentication.AuthUserDetail;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Service
@RequiredArgsConstructor
public class MingleDayUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new BaseException(UserExceptionCode.NOT_EXIST_USER));

        if(user.getStatus() != Status.ACTIVE) {
            throw new BaseException(UserExceptionCode.INACTIVE_USER);
        }

        return new AuthUserDetail(user);
    }
}
