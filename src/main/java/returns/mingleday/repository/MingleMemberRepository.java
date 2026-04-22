package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT m.mingle FROM MingleMember m " +
            "WHERE m.user.userId = :otherId " +
            "AND m.mingle IN (SELECT mm.mingle FROM MingleMember mm WHERE mm.user.userId = :myId) " +
            "AND m.mingle.mingleId <> :currentMingleId")
    List<Mingle> findCommonMingles(Integer myId, Integer otherId, Integer currentMingleId);

    @Query("SELECT m.mingle FROM MingleMember m WHERE m.user = :user")
    List<Mingle> findMingleByUser(User user);

    Boolean existsByMingleAndUser(Mingle mingle, User user);

    void deleteAllByMingle(Mingle mingle);
}
