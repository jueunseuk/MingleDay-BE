package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.schedule.ScheduleMember;

@Repository
public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
}
