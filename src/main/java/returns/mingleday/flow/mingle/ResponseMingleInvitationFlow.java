package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.MingleInvitation;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.ResponseType;
import returns.mingleday.domain.user.User;
import returns.mingleday.global.constant.MailMessageConstant;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.MingleInvitationExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleInvitationService;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseMingleInvitationFlow {

    private final MingleMemberService mingleMemberService;
    private final CreateMingleLogService createMingleLogService;
    private final MingleInvitationService mingleInvitationService;
    private final MinglePermissionService minglePermissionService;
    private final UserRepository userRepository;

    @Transactional
    public String responseMingleInvitation(String token) {
        MingleInvitation mingleInvitation = mingleInvitationService.findMingleInvitationByToken(token);

        if(mingleInvitation.getResponseType() != ResponseType.WAIT) {
            throw new BaseException(MingleInvitationExceptionCode.ALREADY_JOIN_OR_REJECT_MINGLE);
        }

        if(!mingleInvitation.getToken().equals(token)) {
            throw new BaseException(MingleInvitationExceptionCode.INVITATION_TOKEN_MISMATCH);
        }

        Optional<User> optUser = userRepository.findByEmail(mingleInvitation.getTargetEmail());
        if(optUser.isPresent()) {
            User user = optUser.get();

            if(mingleInvitation.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new BaseException(GlobalExceptionCode.EXPIRED_REQUEST);
            }

            MingleMember mingleMember = mingleMemberService.createMingleMember(mingleInvitation.getMingle(), user);
            minglePermissionService.createFullPermissions(mingleMember, !mingleInvitation.getMingle().getUsePermission());
            mingleInvitation.accept();
            createMingleLogService.execute(mingleInvitation.getMingle(), mingleMember, null, MingleLogType.JOIN);
            log.info("An user has accepted the invitation - userId: {}, memberId: {}", user.getUserId(), mingleMember.getMingleMemberId());

            return "success to accept the invitation";
        } else {
            return MailMessageConstant.INVITATION_RESPONSE;
        }
    }
}
