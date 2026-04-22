package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;

import java.util.List;

@Repository
public interface ScheduleInstanceRepository extends JpaRepository<ScheduleInstance, Long> {
    void deleteAllBySchedule(Schedule schedule);
    List<ScheduleInstance> findAllBySchedule(Schedule schedule);
    void deleteAllByScheduleIn(List<Schedule> schedules);
}
