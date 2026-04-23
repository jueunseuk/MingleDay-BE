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
    @JoinColumn(name = "mingle_id", nullable = false)
    private Mingle mingle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_member_id", nullable = false)
    private MingleMember mingleMember;

    @Column(name = "target_email")
    private String targetEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_type")
    private ResponseType responseType;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public static MingleInvitation of(Mingle mingle, MingleMember mingleMember, String email) {
        return MingleInvitation.builder()
                .mingle(mingle)
                .mingleMember(mingleMember)
                .targetEmail(email)
                .responseType(ResponseType.WAIT)
                .expiredAt(LocalDateTime.now().plusHours(3))
                .build();
    }

    public void accept() {
        this.responseType = ResponseType.ACCEPT;
        this.mingle.increaseMemberCnt();
    }

    public void reject() {
        this.responseType = ResponseType.REJECT;
    }
}