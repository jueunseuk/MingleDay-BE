package returns.mingleday.domain.mingle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mingle_invitation")
public class MingleInvitation extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mingle_invitation_id", nullable = false)
    private Long mingleInvitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_member_id", nullable = false)
    private MingleMember mingleMember;

    @Column(name = "target_email")
    private String targetEmail;

    @Column(name = "isResponse")
    private Boolean isResponse;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}