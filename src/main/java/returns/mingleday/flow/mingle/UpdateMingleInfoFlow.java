package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.TargetType;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.UpdateMingleRequest;
import returns.mingleday.repository.MingleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMingleInfoFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleRepository mingleRepository;
    private final MingleMemberService mingleMemberService;
    private final CreateMingleLogService createMingleLogService;

    @Transactional
    public void updateMingleInfo(Integer userId, Integer mingleId, UpdateMingleRequest request) {
        User user = userService.findUserByUserId(userId);

        if(!request.getMingleId().equals(mingleId)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        mingle.updateInfo(
                request.getName(),
                request.getDescription(),
                request.getUsePermission(),
                request.getUseRealname()
        );

        createMingleLogService.execute(mingle, mingleMember, TargetType.MINGLE, MingleLogType.MODIFY);
        log.info("Update a mingle information - userId: {}, mingleId: {}", userId, mingleId);

        mingleRepository.save(mingle);
    }
}
