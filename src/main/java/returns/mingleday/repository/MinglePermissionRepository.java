package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.MinglePermission;
import returns.mingleday.domain.mingle.PermissionType;

@Repository
public interface MinglePermissionRepository extends JpaRepository<MinglePermission, Integer> {
    MinglePermission findByMingleMemberAndPermissionType(MingleMember mingleMember, PermissionType permissionType);
}
