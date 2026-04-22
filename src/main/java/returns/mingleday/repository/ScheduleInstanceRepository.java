package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleInstanceRepository extends JpaRepository<ScheduleInstance, Long> {
    void deleteAllBySchedule(Schedule schedule);
    void deleteAllByScheduleIn(List<Schedule> schedules);

    @Query("select si from ScheduleInstance si where si.schedule.mingle = :mingle and (si.startAt <= :end and si.endAt >= :start) and (si.schedule.isPrivate = false or si.schedule.owner = :user) order by si.startAt asc, si.endAt asc, si.schedule.title asc")
    List<ScheduleInstance> findAllByMingleAndOneMonth(Mingle mingle, User user, LocalDateTime start, LocalDateTime end);
}
