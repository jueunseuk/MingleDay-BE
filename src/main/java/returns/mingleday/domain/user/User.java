package returns.mingleday.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.util.StringParser;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseTime {
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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_updated_at", nullable = false)
    private LocalDateTime passwordUpdatedAt;

    @Column(name = "last_login_at", nullable = false)
    private LocalDateTime lastLoginAt;

    @Column(name = "profile_url")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public static User of(String name, String email, String encodedPassword, String nickname) {
        isValidName(name);
        isValidName(nickname);

        return User.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .password(encodedPassword)
                .passwordUpdatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .profileUrl("")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
    }

    public static void isValidName(String name) {
        if(name == null || name.length() < 2 || name.length() > 10) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        for(int i = 0; i < name.length(); i++) {
            if(StringParser.getCharacterType(name.charAt(i)) == 3) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }
    }

    public static void isValidPassword(String password) {
        if(password == null || password.length() < 8 || password.length() > 20) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        char[] chars = password.toCharArray();
        int alphabet = 0;
        int number = 0;
        int symbol = 0;

        for(char c : chars) {
            switch(StringParser.getCharacterType(c)) {
                case 1: alphabet++; break;
                case 2: number++; break;
                case 3: symbol++; break;
                default: throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }

        if(alphabet < 1 || number < 1 || symbol < 1) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }
    }

    public void resetPassword(String password) {
        this.password = password;
        this.passwordUpdatedAt = LocalDateTime.now();
    }

    public void login() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updateProfileUrl(String path) {
        this.profileUrl = path;
    }

    public void updateProfileInfo(String name, String nickname) {
        isValidName(name);
        isValidName(nickname);

        this.name = name;
        this.nickname = nickname;
    }

    public void toDormant() {
        this.status = Status.DORMANT;
    }
}