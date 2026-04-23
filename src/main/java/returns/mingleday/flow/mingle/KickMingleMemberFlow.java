package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.*;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.MingleMemberExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KickMingleMemberFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleRepository scheduleRepository;
    private final MingleMemberService mingleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final CreateMingleLogService createMingleLogService;
    private final MingleMemberRepository mingleMemberRepository;
    private final MinglePermissionService minglePermissionService;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleInstanceRepository scheduleInstanceRepository;
    private final ScheduleRecurrenceRepository scheduleRecurrenceRepository;

    @Transactional
    public void kickMingleMember(Integer userId, Integer mingleId, Long memberId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember me = mingleMemberService.getMingleMember(mingle, user);
        MingleMember target = mingleMemberService.getMingleMember(memberId);
        User targetUser = target.getUser();

        if(mingle.getUsePermission() && !mingle.getOwner().equals(user) && !minglePermissionService.doesMemberHavePermission(me, PermissionType.EXPULSION)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        if(targetUser.equals(user)) {
            throw new BaseException(MingleMemberExceptionCode.YOU_CANNOT_EXPEL_YOURSELF);
        }

        if(targetUser.equals(mingle.getOwner())) {
            throw new BaseException(MingleMemberExceptionCode.OWNER_OF_MINGLE_CANNOT_BE_EXPELLED);
        }

        List<Schedule> schedules = scheduleSearchService.findScheduleByOwner(targetUser);
        scheduleInstanceRepository.deleteAllByScheduleIn(schedules);
        scheduleMemberRepository.deleteAllByScheduleIn(schedules);
        scheduleRecurrenceRepository.deleteAllByScheduleIn(schedules);
        scheduleRepository.deleteAllInBatch(schedules);

        mingleMemberRepository.delete(target);
        mingle.decreaseMemberCnt();

        createMingleLogService.execute(mingle, me, target, MingleLogType.EXPULSION);
        log.info("Kick mingle member - executorId: {}, targetUserId: {}", me.getMingleMemberId(), targetUser.getUserId());
    }
}
