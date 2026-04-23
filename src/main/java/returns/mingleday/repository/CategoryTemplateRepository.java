package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.category.CategoryTemplate;
import returns.mingleday.domain.mingle.MingleType;

import java.util.List;

@Repository
public interface CategoryTemplateRepository extends JpaRepository<CategoryTemplate, Integer> {
    List<CategoryTemplate> findAllByMingleType(MingleType mingleType);
}
