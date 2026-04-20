package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.MingleLog;

@Repository
public interface MingleLogRepository extends JpaRepository<MingleLog, Long> {
}
