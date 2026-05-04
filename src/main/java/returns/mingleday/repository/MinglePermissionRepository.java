package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.MinglePermission;
import returns.mingleday.domain.mingle.PermissionType;

import java.util.List;

@Repository
public interface MinglePermissionRepository extends JpaRepository<MinglePermission, Integer> {
    MinglePermission findByMingleMemberAndPermissionType(MingleMember mingleMember, PermissionType permissionType);

    @Query("""
    select mp
    from MinglePermission mp
    where mp.mingleMember.mingle = :mingle
    order by mp.mingleMember.mingleMemberId asc
    """)
    List<MinglePermission> findAllByMingleOrderByMemberId(Mingle mingle);

    List<MinglePermission> findAllByMingleMemberOrderByPermissionType(MingleMember targetMember);
}
