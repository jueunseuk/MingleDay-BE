package returns.mingleday.model.category;

import lombok.Data;

@Data
public class UpsertCategoryRequest {
    private Long categoryId;
    private Integer mingleId;
    private String name;
    private String description;
    private String backgroundColor;
    private String textColor;
}
