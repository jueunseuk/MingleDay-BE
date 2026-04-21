package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.mingle.Mingle;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByMingle(Mingle mingle);

    Optional<Category> findCategoryByCategoryIdAndMingle(Long categoryId, Mingle mingle);
}
