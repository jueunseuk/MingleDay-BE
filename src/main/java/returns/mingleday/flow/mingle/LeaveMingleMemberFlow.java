package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.*;
import returns.mingleday.response.code.MingleMemberExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveMingleMemberFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleRepository scheduleRepository;
    private final MingleMemberService mingleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final CreateMingleLogService createMingleLogService;
    private final MingleMemberRepository mingleMemberRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleInstanceRepository scheduleInstanceRepository;
    private final ScheduleRecurrenceRepository scheduleRecurrenceRepository;

    @Transactional
    public void leaveMingleMember(Integer userId, Integer mingleId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember me = mingleMemberService.getMingleMember(mingle, user);

        if(mingle.getMemberCnt() > 1 && mingle.getOwner().equals(user)) {
            throw new BaseException(MingleMemberExceptionCode.OWNER_CANNOT_LEAVE);
        }

        if(mingle.getMemberCnt() > 1) {
            List<Schedule> schedules = scheduleSearchService.findScheduleByOwner(user);
            scheduleInstanceRepository.deleteAllByScheduleIn(schedules);
            scheduleMemberRepository.deleteAllByScheduleIn(schedules);
            scheduleMemberRepository.deleteAllByMingleMember(me);
            scheduleRecurrenceRepository.deleteAllByScheduleIn(schedules);
            scheduleRepository.deleteAllInBatch(schedules);

            mingleMemberRepository.delete(me);
            mingle.decreaseMemberCnt();
            createMingleLogService.execute(mingle, me, null, MingleLogType.LEAVE);
        } else {
            mingleService.deleteMingleWithAllData(mingle);
        }

        log.info("Leave mingle - executorId: {}", userId);
    }
}
