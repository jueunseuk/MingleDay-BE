package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;

@Repository
public interface MingleRepository extends JpaRepository<Mingle, Integer> {
}
