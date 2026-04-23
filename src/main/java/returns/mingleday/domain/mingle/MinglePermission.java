package returns.mingleday.domain.mingle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mingle_permission", uniqueConstraints = @UniqueConstraint(columnNames = {"mingle_member_id", "permission_type"}))
public class MinglePermission extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mingle_permission_id", nullable = false)
    private Long minglePermissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_member_id", nullable = false)
    private MingleMember mingleMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type")
    private PermissionType permissionType;

    @Column(name = "value")
    private Boolean value;

    public void updateValue(Boolean value) {
        this.value = value;
    }
}