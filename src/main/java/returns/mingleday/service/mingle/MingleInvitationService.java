package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleInvitation;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.Email;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.MingleInvitationResponse;
import returns.mingleday.repository.MingleInvitationRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MingleInvitationService {

    private final MingleInvitationRepository mingleInvitationRepository;
    private final UserService userService;

    @Transactional
    public void createMingleInvitation(Mingle mingle, MingleMember mingleMember, String email) {
        Email.validEmail(email);

        MingleInvitation mingleInvitation = MingleInvitation.of(mingle, mingleMember, email);
        mingleInvitationRepository.save(mingleInvitation);
    }

    public MingleInvitation getMingleInvitation(Long id) {
        return mingleInvitationRepository.findById(id)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    public List<MingleInvitationResponse> getAllMingleInvitations(Integer userId) {
        User user = userService.findUserByUserId(userId);
        List<MingleInvitation> mingleInvitations = mingleInvitationRepository.findAllByTargetEmailOrderByCreatedAtDesc(user.getEmail());
        return mingleInvitations.stream().map(MingleInvitationResponse::new).toList();
    }
}
