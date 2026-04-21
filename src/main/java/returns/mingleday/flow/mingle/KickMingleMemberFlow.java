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
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.schedule.ScheduleMemberService;
import returns.mingleday.service.schedule.ScheduleRecurrenceService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.schedule.ScheduleService;
import returns.mingleday.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KickMingleMemberFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleService scheduleService;
    private final MingleMemberService mingleMemberService;
    private final ScheduleMemberService scheduleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final CreateMingleLogService createMingleLogService;
    private final MinglePermissionService minglePermissionService;
    private final ScheduleRecurrenceService scheduleRecurrenceService;

    @Transactional
    public void kickMingleMember(Integer userId, Integer mingleId, Long memberId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember me = mingleMemberService.getMingleMember(mingle, user);
        MingleMember target = mingleMemberService.getMingleMember(memberId);

        if(mingle.getUsePermission() && !mingle.getOwner().equals(user) && minglePermissionService.doesMemberHavePermission(me, PermissionType.EXPULSION)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        List<Schedule> schedules = scheduleSearchService.findScheduleByMember(user);
        for(Schedule schedule : schedules) {
            scheduleRecurrenceService.deleteAllBySchedule(schedule);
            scheduleMemberService.deleteAllBySchedule(schedule);
            scheduleService.deleteAllBySchedule(schedule);
            scheduleService.deleteSchedule(schedule);
        }
        createMingleLogService.execute(mingle, me, target, MingleLogType.EXPULSION);

        log.info("Kick mingle member - executorId: {}, targetId: {}", me.getMingleMemberId(), target.getMingleMemberId());
    }
}
