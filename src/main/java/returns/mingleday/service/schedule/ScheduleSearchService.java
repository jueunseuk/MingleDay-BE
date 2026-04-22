package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.ScheduleInstanceRepository;
import returns.mingleday.repository.ScheduleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleSearchService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleInstanceRepository scheduleInstanceRepository;

    public List<Schedule> findScheduleByCategory(Category category) {
        return scheduleRepository.findAllByCategory(category);
    }

    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    public ScheduleInstance findScheduleInstanceById(Long scheduleInstanceId) {
        return scheduleInstanceRepository.findById(scheduleInstanceId)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    public List<Schedule> findScheduleByOwner(User user) {
        return scheduleRepository.findAllByOwner(user);
    }

    public List<Schedule> findScheduleByMingle(Mingle mingle) {
        return scheduleRepository.findAlLByMingle(mingle);
    }


}
