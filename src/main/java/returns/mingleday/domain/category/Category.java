package returns.mingleday.domain.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.CategoryExceptionCode;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.util.StringParser;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category", uniqueConstraints = @UniqueConstraint(columnNames = {"mingle_id", "name"}))
public class Category extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_id")
    private Mingle mingle;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "background_color", length = 6)
    private String backgroundColor;

    @Column(name = "text_color", length = 6)
    private String textColor;

    public static Category of(Mingle mingle, String name, String description, String backgroundColor, String textColor) {
        isValidColor(backgroundColor);
        isValidColor(textColor);
        isValidName(name);

        return Category.builder()
                .mingle(mingle)
                .name(name)
                .description(description)
                .textColor(textColor)
                .backgroundColor(backgroundColor)
                .build();
    }

    public void update(String name, String description, String backgroundColor, String textColor) {
        isValidColor(backgroundColor);
        isValidColor(textColor);
        isValidName(name);

        this.name = name;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public static void isValidName(String name) {
        if(name == null || name.isEmpty() || name.length() > 15) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        for(int i = 0; i < name.length(); i++) {
            if(StringParser.getCharacterType(name.charAt(i)) == 3) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }
    }

    public static void isValidColor(String color) {
        if(color == null || color.length() != 6) {
            throw new BaseException(CategoryExceptionCode.COLOR_CODE_MUST_BE_SIX_DIGITS);
        }
    }
}
