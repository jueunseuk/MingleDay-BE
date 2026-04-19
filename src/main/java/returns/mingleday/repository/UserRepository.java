package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
