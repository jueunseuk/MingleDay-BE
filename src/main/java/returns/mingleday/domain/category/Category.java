package returns.mingleday.domain.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.global.domain.BaseTime;

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

    @Column(name = "background_color", length = 7)
    private String backgroundColor;

    @Column(name = "text_color", length = 7)
    private String textColor;
}
