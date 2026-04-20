package returns.mingleday.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.EmailExceptionCode;
import returns.mingleday.response.code.GlobalExceptionCode;
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

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "verify_cnt", nullable = false)
    private Integer verifyCnt;

    public static Email of(String email, Purpose purpose, String authCode) {
        validEmail(email);

        long timeToExpire;
        switch(purpose) {
            case REGISTER -> timeToExpire = 10;
            case REISSUE -> timeToExpire = 5;
            case WITHDRAWAL -> timeToExpire = 3;
            default -> throw new BaseException(EmailExceptionCode.INCORRECT_MAIL_CODE_PURPOSE);
        }

        return Email.builder()
                .email(email)
                .purpose(purpose)
                .expiredAt(LocalDateTime.now().plusMinutes(timeToExpire))
                .code(authCode)
                .isVerified(false)
                .verifyCnt(0)
                .build();
    }

    public static void validEmail(String email) {
        if(email == null || email.length() <= 3 || !email.contains("@") || !email.contains(".")) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }
    }

    public void verify() {
        this.verifyCnt++;
        if(LocalDateTime.now().isAfter(expiredAt)) {
            throw new BaseException(UserExceptionCode.AUTHENTICATED_TIME_EXPIRES);
        }
        this.isVerified = true;
    }

    public void expirationProcess() {
        this.expiredAt = LocalDateTime.now();
    }
}