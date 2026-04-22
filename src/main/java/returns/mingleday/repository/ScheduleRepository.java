package returns.mingleday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByCategory(Category category);

    List<Schedule> findAllByOwner(User user);

    List<Schedule> findAlLByMingle(Mingle mingle);

    void deleteAllIn(List<Schedule> schedules);
}
