package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.MinglePermissionRequest;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePermissionFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;
    private final MinglePermissionService minglePermissionService;

    @Transactional
    public void updatePermission(Integer userId, MinglePermissionRequest request, Integer mingleId, Long mingleMemberId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        MingleMember mingleMember = mingleMemberService.getMingleMember(mingleMemberId);
        if(!mingleMember.getMingle().equals(mingle)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        minglePermissionService.updateMinglePermissions(mingleMember, request.getPermission(), request.getValue());
        log.info("Request for update permission of mingle member - memberId: {}, body: {} -> {}", mingleMemberId, request.getPermission(),request.getValue());
    }
}
