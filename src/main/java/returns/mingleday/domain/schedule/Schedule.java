package returns.mingleday.domain.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.mingle.Mingle;
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
@Table(name = "schedule")
public class Schedule extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_id")
    private Mingle mingle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_repeated")
    private Boolean isRepeated;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "is_private")
    private Boolean isPrivate;

    public static Schedule of(Mingle mingle, User owner, String title, String content, String location, Category category, Boolean isRepeated, Boolean isLocked, Boolean isPrivate) {
        isValidName(title);

        return Schedule.builder()
                .mingle(mingle)
                .owner(owner)
                .title(title)
                .content(content)
                .location(location)
                .category(category)
                .isRepeated(isRepeated)
                .isLocked(isLocked)
                .isPrivate(isPrivate)
                .build();
    }

    public void update(String title, String content, String location, Category category, Boolean isLocked, Boolean isPrivate) {
        isValidName(title);

        this.title = title;
        this.content = content;
        this.location = location;
        this.category = category;
        this.isRepeated = isLocked;
        this.isLocked = isPrivate;
    }

    private static void isValidName(String title) {
        if(title == null || title.isEmpty() || title.length() > 30) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        for(int i = 0; i < title.length(); i++) {
            if(StringParser.getCharacterType(title.charAt(i)) == 3) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }
    }

    public void cleanCategory() {
        this.category = null;
    }
}
