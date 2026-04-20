package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.user.Email;
import returns.mingleday.domain.user.Purpose;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findFirstByEmailAndPurpose(String email, Purpose purpose);
}
