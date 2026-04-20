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
@Table(name = "mingle")
public class Mingle extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mingle_id", nullable = false)
    private Integer mingleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "use_permission")
    private Boolean usePermission;

    @Column(name = "use_realname")
    private Boolean useRealname;

    @Enumerated(EnumType.STRING)
    @Column(name = "mingle_type")
    private MingleType mingleType;
}