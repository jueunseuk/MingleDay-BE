package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.image.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
