package returns.mingleday.flow.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.ScheduleMemberRequest;
import returns.mingleday.repository.ScheduleMemberRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.schedule.ScheduleMemberService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateScheduleMemberFlow {

    private final UserService userService;
    private final MingleMemberService mingleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final MinglePermissionService minglePermissionService;
    private final ScheduleMemberService scheduleMemberService;
    private final ScheduleMemberRepository scheduleMemberRepository;

    @Transactional
    public void updateScheduleMember(Integer userId, Long scheduleId, List<ScheduleMemberRequest> request) {
        User user = userService.findUserByUserId(userId);
        Schedule schedule = scheduleSearchService.findScheduleById(scheduleId);
        Mingle mingle = schedule.getMingle();
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);

        if(mingle.getUsePermission() &&
                !mingle.getOwner().equals(user) &&
                !schedule.getOwner().equals(user) &&
                !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.MODIFY)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        scheduleMemberService.deleteAllBySchedule(schedule);

        List<ScheduleMember> newScheduleMembers = new ArrayList<>();
        for(ScheduleMemberRequest sm : request) {
            newScheduleMembers.add(ScheduleMember.of(
                    schedule,
                    mingleMemberService.getMingleMember(sm.getMingleMemberId()),
                    sm.getMemo()));
        }

        scheduleMemberRepository.saveAll(newScheduleMembers);
    }
}
