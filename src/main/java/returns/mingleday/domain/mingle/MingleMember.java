package returns.mingleday.domain.mingle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.user.User;
import returns.mingleday.global.domain.BaseTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mingle_member", uniqueConstraints = @UniqueConstraint(columnNames = {"mingle_id", "user_id"}))
public class MingleMember extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mingle_member_id", nullable = false)
    private Long mingleMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_id", nullable = false)
    private Mingle mingle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static MingleMember of(Mingle mingle, User user) {
        return MingleMember.builder().mingle(mingle).user(user).build();
    }

    public String getDisplayName() {
        if (this.mingle.getUseRealname()) {
            return this.user.getName();
        }
        return this.user.getNickname();
    }
}