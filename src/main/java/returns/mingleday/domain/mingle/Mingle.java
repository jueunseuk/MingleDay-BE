package returns.mingleday.domain.mingle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.user.User;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.util.StringParser;

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

    public static Mingle of(User owner, String name, String description, String profileUrl, Boolean usePermission, Boolean useRealname, MingleType mingleType) {
        isValidName(name);

        return Mingle.builder()
                .owner(owner)
                .name(name)
                .description(description)
                .profileUrl(profileUrl)
                .usePermission(usePermission != null && usePermission)
                .useRealname(useRealname != null && useRealname)
                .mingleType(mingleType)
                .build();
    }

    private static void isValidName(String name) {
        if(name == null || name.length() < 2 || name.length() > 20) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        for(int i = 0; i < name.length(); i++) {
            if(StringParser.getCharacterType(name.charAt(i)) == 3) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }
    }
}