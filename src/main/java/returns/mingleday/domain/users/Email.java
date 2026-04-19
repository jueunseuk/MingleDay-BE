package returns.mingleday.domain.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email")
public class Email extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private Purpose purpose;

    @Column(name = "is_verified")
    private Boolean isVerified;

    public void verify() {
        if(LocalDateTime.now().isAfter(expiredAt)) {
            throw new BaseException(UserExceptionCode.AUTHENTICATED_TIME_EXPIRES);
        }
        this.isVerified = true;
    }
}