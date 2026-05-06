package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.MingleInvitation;

import java.util.List;
import java.util.Optional;

@Repository
public interface MingleInvitationRepository extends JpaRepository<MingleInvitation, Long> {
    List<MingleInvitation> findAllByTargetEmailOrderByCreatedAtDesc(String email);
    Optional<MingleInvitation> findMingleInvitationByToken(String token);
}
