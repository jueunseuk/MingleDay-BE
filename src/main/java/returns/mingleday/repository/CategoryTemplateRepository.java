package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.category.CategoryTemplate;
import returns.mingleday.domain.mingle.MingleType;

import java.util.List;

@Repository
public interface CategoryTemplateRepository extends JpaRepository<CategoryTemplate, Integer> {
    @Query("select ct from CategoryTemplate ct where ct.mingleType = :mingleType or ct.mingleType = returns.mingleday.domain.mingle.MingleType.COMMON")
    List<CategoryTemplate> findAllByMingleType(MingleType mingleType);
}
