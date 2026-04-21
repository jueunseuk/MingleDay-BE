package returns.mingleday.flow.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.schedule.ScheduleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.*;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.schedule.ScheduleMemberService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.schedule.ScheduleService;
import returns.mingleday.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateScheduleInstanceFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleService scheduleService;
    private final MingleMemberService mingleMemberService;
    private final ScheduleMemberService scheduleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final MinglePermissionService minglePermissionService;

    @Transactional
    public DetailScheduleResponse updateScheduleInstance(Integer userId, Integer mingleId, Long scheduleId, Long scheduleInstanceId, UpdateScheduleInstanceRequest request) {
        User user = userService.findUserByUserId(userId);

        if(!request.getMingleId().equals(mingleId) || !request.getScheduleInstanceId().equals(scheduleInstanceId)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }
        Mingle mingle = mingleService.findMingleById(mingleId);

        Schedule schedule = scheduleSearchService.findScheduleById(scheduleId);
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() && !mingle.getOwner().equals(user) && !schedule.getOwner().equals(user) && !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.MODIFY)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        ScheduleInstance scheduleInstance = scheduleSearchService.findScheduleInstanceById(scheduleId);
        if(!scheduleInstance.getSchedule().equals(schedule)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }

        scheduleService.updateScheduleInstance(
                scheduleInstance,
                request.getStartAt(),
                request.getEndAt(),
                request.getMemo()
        );

        List<ScheduleMember> scheduleMembers = scheduleMemberService.findScheduleMemberBySchedule(schedule);
        List<ScheduleMemberResponse> scheduleMemberResponses = scheduleMembers.stream().map(ScheduleMemberResponse::new).toList();

        log.info("Update Schedule Instance - userId: {}, scheduleId: {}, scheduleInstanceId: {}", userId, scheduleId, scheduleInstanceId);
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