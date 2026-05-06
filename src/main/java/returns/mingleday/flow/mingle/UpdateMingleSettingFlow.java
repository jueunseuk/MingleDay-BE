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
import returns.mingleday.repository.MingleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.MingleExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMingleSettingFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleRepository mingleRepository;
    private final MingleMemberService mingleMemberService;
    private final CreateMingleLogService createMingleLogService;

    @Transactional
    public void updateMingleSetting(Integer userId, Integer mingleId, String option, Boolean value) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);

        if(option.equals("realname")) {
            mingle.updateUseRealname(value);
        } else if(option.equals("permission")) {
            mingle.updateUsePermission(value);
        } else {
            throw new BaseException(MingleExceptionCode.INVALID_MINGLE_SETTING_OPTION);
        }

        createMingleLogService.execute(mingle, mingleMember, TargetType.MINGLE, MingleLogType.MODIFY);
        log.info("Update a mingle setting - userId: {}, mingleId: {}", userId, mingleId);

        mingleRepository.save(mingle);
    }
}
