package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.CreateScheduleRequest;
import returns.mingleday.repository.ScheduleInstanceRepository;
import returns.mingleday.repository.ScheduleRepository;
import returns.mingleday.service.category.CategoryService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CategoryService categoryService;
    private final ScheduleInstanceRepository scheduleInstanceRepository;

    @Transactional
    public Schedule createSchedule(Mingle mingle, User user, CreateScheduleRequest request) {
        Schedule schedule = Schedule.of(
                mingle,
                user,
                request.getTitle(),
                request.getContent().isEmpty() ? "" : request.getContent(),
                request.getLocation().isEmpty() ? "" : request.getLocation(),
                categoryService.findCategoryByIdAndMingle(request.getCategoryId(), mingle),
                request.getIsRepeated(),
                request.getIsLocked(),
                request.getIsPrivate()
        );

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public ScheduleInstance createSolidScheduleInstance(Schedule schedule, LocalDateTime start, LocalDateTime end, String memo) {
        ScheduleInstance scheduleInstance = ScheduleInstance.of(schedule, start, end, memo);
        return scheduleInstanceRepository.save(scheduleInstance);
    }

    @Transactional
    public ScheduleInstance createSolidScheduleInstance(Schedule schedule, LocalDateTime start, LocalDateTime end) {
        return createSolidScheduleInstance(schedule, start, end, "");
    }

    @Transactional
    public ScheduleInstance createRecurrenceScheduleInstance(Schedule schedule, LocalDateTime start, LocalDateTime end, String memo) {
        ScheduleInstance scheduleInstance = ScheduleInstance.of(schedule, start, end, memo);
        return scheduleInstanceRepository.save(scheduleInstance);
    }

    @Transactional
    public ScheduleInstance createRecurrenceScheduleInstance(Schedule schedule, LocalDateTime start, LocalDateTime end) {
        return createRecurrenceScheduleInstance(schedule, start, end, "");
    }
}
