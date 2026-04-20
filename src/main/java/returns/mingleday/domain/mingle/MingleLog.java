package returns.mingleday.domain.mingle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.users.User;
import returns.mingleday.global.domain.BaseTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mingle_log")
public class MingleLog extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mingle_log_id", nullable = false)
    private Long mingleLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_id", nullable = false)
    private Mingle mingle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "mingle_log_type")
    private MingleLogType mingleLogType;
}