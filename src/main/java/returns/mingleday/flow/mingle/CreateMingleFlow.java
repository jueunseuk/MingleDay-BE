package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.CreateMingleRequest;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMingleFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;
    private final MinglePermissionService minglePermissionService;

    @Transactional
    public Integer createMingle(Integer userId, CreateMingleRequest request) {
        User user = userService.findUserByUserId(userId);

        Mingle mingle = mingleService.createMingle(user, request);
        log.info("Create new mingle - owner: {}, mingleId: {}", userId, mingle.getMingleId());

        MingleMember mingleMember = mingleMemberService.createMingleMember(mingle, user);

        minglePermissionService.createFullPermissions(mingleMember, true);
        log.info("Create full permissions for member: {}", mingleMember.getMingleMemberId());

        return mingle.getMingleId();
    }
}
