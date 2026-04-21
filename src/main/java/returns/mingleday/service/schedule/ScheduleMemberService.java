package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleMember;
import returns.mingleday.repository.ScheduleMemberRepository;

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
}
