package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MingleMemberRepository extends JpaRepository<MingleMember, Long> {
    Optional<MingleMember> findByMingleAndUser(Mingle mingle, User user);
    List<MingleMember> findAllByMingle(Mingle mingle);
}
