package returns.mingleday.flow.schedule;

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
import returns.mingleday.repository.ScheduleInstanceRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteScheduleFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleService scheduleService;
    private final MingleMemberService mingleMemberService;
    private final ScheduleMemberService scheduleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final CreateMingleLogService createMingleLogService;
    private final MinglePermissionService minglePermissionService;
    private final ScheduleRecurrenceService scheduleRecurrenceService;
    private final ScheduleInstanceRepository scheduleInstanceRepository;

    @Transactional
    public void deleteSchedule(Integer userId, Integer mingleId, Long scheduleId) {
        log.info("Delete schedule process start - userId: {}, mingleId: {}, scheduleId: {}", userId, mingleId, scheduleId);

        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        Schedule schedule = scheduleSearchService.findScheduleById(scheduleId);
        if(mingle.getUsePermission() &&
                !mingle.getOwner().equals(user) &&
                !schedule.getOwner().equals(user) &&
                !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.DELETE)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        scheduleRecurrenceService.deleteAllBySchedule(schedule);
        scheduleMemberService.deleteAllBySchedule(schedule);
        scheduleInstanceRepository.deleteAllBySchedule(schedule);

        scheduleService.deleteSchedule(schedule);

        createMingleLogService.execute(mingle, mingleMember, schedule, MingleLogType.DELETE);
        log.info("Delete schedule process success - mingleId: {}, userId: {}, scheduleId: {}", mingleId, userId, scheduleId);
    }
}
