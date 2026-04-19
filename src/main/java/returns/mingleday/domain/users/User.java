package returns.mingleday.domain.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "password_updated_at")
    private LocalDateTime passwordUpdatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}