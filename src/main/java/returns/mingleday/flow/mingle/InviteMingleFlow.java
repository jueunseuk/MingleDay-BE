package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.InviteMingleRequest;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleInvitationService;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;
import returns.mingleday.util.StringMasking;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteMingleFlow {

    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;
    private final MingleInvitationService mingleInvitationService;
    private final UserService userService;
    private final MinglePermissionService minglePermissionService;

    @Transactional
    public void inviteMingle(Integer userId, InviteMingleRequest request) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(request.getMingleId());
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);

        if(mingle.getUsePermission() &&
                !mingle.getOwner().equals(user) &&
                !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.INVITE)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        mingleInvitationService.createMingleInvitation(mingle, mingleMember, request.getEmail());
        log.info("Sent an invitation to Mingle to that email - senderId: {}, email: {}", userId, StringMasking.emailMasking(request.getEmail()));
    }
}
