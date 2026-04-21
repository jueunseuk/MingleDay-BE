package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveMingleMemberFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;
    private final CreateMingleLogService createMingleLogService;

    @Transactional
    public void leaveMingleMember(Integer userId, Integer mingleId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember me = mingleMemberService.getMingleMember(mingle, user);

        // 내부 로직 나중 구현
        mingleMemberService.deleteMingleMember();
        createMingleLogService.execute(mingle, me, null, MingleLogType.LEAVE);

        log.info("Leave mingle member - executorId: {}", me.getMingleMemberId());
    }
}
