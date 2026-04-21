package returns.mingleday.domain.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_template")
public class CategoryTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_template_id", nullable = false)
    private Integer categoryTemplateId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "background_color", length = 6)
    private String backgroundColor;

    @Column(name = "text_color", length = 6)
    private String textColor;
}
