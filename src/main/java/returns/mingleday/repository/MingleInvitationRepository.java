package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleInvitation;

import java.util.List;
import java.util.Optional;

@Repository
public interface MingleInvitationRepository extends JpaRepository<MingleInvitation, Long> {
    Optional<MingleInvitation> findByMingleAndTargetEmail(Mingle mingle, String email);

    List<MingleInvitation> findAllByTargetEmailOrderByCreatedAtDesc(String email);
}
