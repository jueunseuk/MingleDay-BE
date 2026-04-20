package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.MingleInvitation;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.ResponseType;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.ResponseMingleRequest;
import returns.mingleday.model.mingle.ResponseMingleResponse;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.MingleInvitationExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleInvitationService;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseMingleInvitationFlow {

    private final UserService userService;
    private final MingleInvitationService mingleInvitationService;
    private final MingleMemberService mingleMemberService;
    private final MinglePermissionService minglePermissionService;

    @Transactional
    public ResponseMingleResponse responseMingleInvitation(Integer userId, ResponseMingleRequest request) {
        User user = userService.findUserByUserId(userId);

        MingleInvitation mingleInvitation = mingleInvitationService.getMingleInvitation(request.getMingleInvitationId());

        if(mingleInvitation.getResponseType() != ResponseType.WAIT) {
            throw new BaseException(MingleInvitationExceptionCode.ALREADY_JOIN_OR_REJECT_MINGLE);
        }

        if (!mingleInvitation.getTargetEmail().equals(user.getEmail())) {
            throw new BaseException(MingleInvitationExceptionCode.NOT_INVITED_USER_TO_THIS_MINGLE);
        }

        if(mingleInvitation.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BaseException(GlobalExceptionCode.EXPIRED_REQUEST);
        }

        if(request.getResponseType() == ResponseType.ACCEPT) {
            MingleMember mingleMember = mingleMemberService.createMingleMember(mingleInvitation.getMingle(), user);
            minglePermissionService.createFullPermissions(mingleMember, false);
            mingleInvitation.accept();
            log.info("An user has accepted the invitation - userId: {}, memberId: {}", userId, mingleMember.getMingleMemberId());
        } else {
            mingleInvitation.reject();
            log.info("An user has rejected the invitation - userId: {}", userId);
        }

        return new ResponseMingleResponse(mingleInvitation.getMingle().getMingleId());
    }
}
