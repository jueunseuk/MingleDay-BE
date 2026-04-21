package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.category.CategoryTemplate;

@Repository
public interface CategoryTemplateRepository extends JpaRepository<CategoryTemplate, Integer> {
}
