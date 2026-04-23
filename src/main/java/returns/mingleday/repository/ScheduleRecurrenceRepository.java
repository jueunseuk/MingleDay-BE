package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleRecurrence;

import java.util.List;

@Repository
public interface ScheduleRecurrenceRepository extends JpaRepository<ScheduleRecurrence, Long> {
    void deleteAllBySchedule(Schedule schedule);

    void deleteAllByScheduleIn(List<Schedule> schedules);
}
