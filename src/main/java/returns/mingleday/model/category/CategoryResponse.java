package returns.mingleday.model.category;

import lombok.Data;
import returns.mingleday.domain.category.Category;

@Data
public class CategoryResponse {
    private Long categoryId;
    private String name;
    private String description;
    private String backgroundColor;
    private String textColor;

    public CategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.backgroundColor = category.getBackgroundColor();
        this.textColor = category.getTextColor();
    }
}
