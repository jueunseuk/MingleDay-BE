package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.schedule.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
