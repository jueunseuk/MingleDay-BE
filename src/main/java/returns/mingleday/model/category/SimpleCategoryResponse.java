package returns.mingleday.model.category;

import lombok.Data;
import returns.mingleday.domain.category.Category;

@Data
public class SimpleCategoryResponse {
    private Long categoryId;
    private String name;
    private String backgroundColor;
    private String textColor;

    public SimpleCategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.backgroundColor = category.getBackgroundColor();
        this.textColor = category.getTextColor();
    }
}
