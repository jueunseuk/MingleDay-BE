package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.schedule.ScheduleInstance;

@Repository
public interface ScheduleInstanceRepository extends JpaRepository<ScheduleInstance, Long> {
}
