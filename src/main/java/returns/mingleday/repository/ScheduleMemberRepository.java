package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleMember;

import java.util.List;

@Repository
public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    List<ScheduleMember> findAllBySchedule(Schedule schedule);

    void deleteAllBySchedule(Schedule schedule);

    void deleteAllByScheduleIn(List<Schedule> schedules);

    void deleteAllByMingleMember(MingleMember me);
}
