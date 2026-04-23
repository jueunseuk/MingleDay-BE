package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleMember;
import returns.mingleday.repository.ScheduleMemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleMemberService {

    private final ScheduleMemberRepository scheduleMemberRepository;

    @Transactional
    public ScheduleMember createScheduleMember(Schedule schedule, MingleMember mingleMember, String memo) {
        return ScheduleMember.of(
                schedule,
                mingleMember,
                memo.isEmpty() ? "" : memo
        );
    }

    public List<ScheduleMember> findScheduleMemberBySchedule(Schedule schedule) {
        return scheduleMemberRepository.findAllBySchedule(schedule);
    }

    public void deleteAllBySchedule(Schedule schedule) {
        scheduleMemberRepository.deleteAllBySchedule(schedule);
        log.info("delete all schedule member - scheduleId: {}", schedule.getScheduleId());
    }
}
