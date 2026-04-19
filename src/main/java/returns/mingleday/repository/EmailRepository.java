package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.users.Email;
import returns.mingleday.domain.users.Purpose;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Email findFirstByEmailAndPurpose(String email, Purpose purpose);
}
