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
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.schedule.ScheduleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.*;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.category.CategoryService;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.schedule.ScheduleMemberService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.schedule.ScheduleService;
import returns.mingleday.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateScheduleFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final CategoryService categoryService;
    private final ScheduleService scheduleService;
    private final MingleMemberService mingleMemberService;
    private final ScheduleMemberService scheduleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final CreateMingleLogService createMingleLogService;
    private final MinglePermissionService minglePermissionService;

    @Transactional
    public DetailScheduleResponse updateSchedule(Integer userId, Integer mingleId, Long scheduleId, UpdateScheduleRequest request) {
        User user = userService.findUserByUserId(userId);

        if(!request.getMingleId().equals(mingleId)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }
        Mingle mingle = mingleService.findMingleById(mingleId);

        Schedule schedule = scheduleSearchService.findScheduleById(scheduleId);
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() && !mingle.getOwner().equals(user)  && !schedule.getOwner().equals(user) && !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.MODIFY)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        scheduleService.updateSchedule(
                schedule,
                request.getTitle(),
                request.getContent(),
                request.getLocation(),
                categoryService.findCategoryById(request.getCategoryId()),
                request.getIsLocked(),
                request.getIsPrivate()
        );

        ScheduleInstance scheduleInstance = scheduleSearchService.findScheduleInstanceById(scheduleId);

        List<ScheduleMember> scheduleMembers = scheduleMemberService.findScheduleMemberBySchedule(schedule);
        List<ScheduleMemberResponse> scheduleMemberResponses = scheduleMembers.stream().map(ScheduleMemberResponse::new).toList();

        createMingleLogService.execute(mingle, mingleMember, schedule, MingleLogType.MODIFY);
        log.info("Update Schedule Instance - userId: {}, scheduleId: {}", userId, scheduleId);
        return new DetailScheduleResponse(
                schedule,
                scheduleMemberResponses,
                new ScheduleInstanceResponse(
                        scheduleInstance,
                        new SimpleScheduleInstanceResponse(scheduleInstance.getPrevScheduleInstance()),
                        new SimpleScheduleInstanceResponse(scheduleInstance.getNextScheduleInstance())
                )
        );
    }
}
