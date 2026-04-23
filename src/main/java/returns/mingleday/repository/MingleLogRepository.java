package returns.mingleday.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLog;

import java.util.List;

@Repository
public interface MingleLogRepository extends JpaRepository<MingleLog, Long> {
    void deleteAllByMingle(Mingle mingle);

    List<MingleLog> findAllByMingleInOrderByCreatedAt(List<Mingle> mingles, Pageable createdAt);

    List<MingleLog> findAllByMingleOrderByCreatedAt(Mingle mingle, Pageable createdAt);
}
