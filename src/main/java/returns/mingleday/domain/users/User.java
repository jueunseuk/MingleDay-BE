package returns.mingleday.domain.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.time.LocalDateTime;
import java.util.Set;

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
            if(getCharacterType(name.charAt(i)) == 3) {
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
            switch(getCharacterType(c)) {
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

    private static Integer getCharacterType(char c) {
        if(Character.isLetter(c)) {
            return 1;
        } else if(Character.isDigit(c)) {
            return 2;
        } else if(SPECIAL_CHARS.contains(c)) {
            return 3;
        }

        return -1;
    }

    private static final Set<Character> SPECIAL_CHARS = Set.of(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '[', ']', '{', '}', '.', '?'
    );

    public void login() {
        this.lastLoginAt = LocalDateTime.now();
    }
}