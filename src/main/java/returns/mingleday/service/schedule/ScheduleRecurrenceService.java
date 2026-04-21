package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.schedule.EndType;
import returns.mingleday.domain.schedule.RepeatType;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleRecurrence;
import returns.mingleday.repository.ScheduleRecurrenceRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleRecurrenceService {

    private final ScheduleRecurrenceRepository scheduleRecurrenceRepository;

    public ScheduleRecurrence findById(Long scheduleRecurrenceId) {
        return scheduleRecurrenceRepository.findById(scheduleRecurrenceId)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    @Transactional
    public ScheduleRecurrence createScheduleRecurrence(Schedule schedule, RepeatType repeatType, String repeatValue, EndType endType, String endValue) {
        if(repeatType == RepeatType.INTERVAL && repeatValue.equals("1")) {
            repeatType = RepeatType.DAILY;
        }

        ScheduleRecurrence scheduleRecurrence = ScheduleRecurrence.of(schedule, repeatType, repeatValue, endType, endValue);
        return scheduleRecurrenceRepository.save(scheduleRecurrence);
    }

    @Transactional
    public ScheduleRecurrence updateScheduleRecurrence(Long scheduleRecurrenceId, RepeatType repeatType, String repeatValue, EndType endType, String endValue) {
        ScheduleRecurrence scheduleRecurrence = findById(scheduleRecurrenceId);

        if(repeatType == RepeatType.INTERVAL && repeatValue.equals("1")) {
            repeatType = RepeatType.DAILY;
        }

        scheduleRecurrence.update(repeatType, repeatValue, endType, endValue);

        return scheduleRecurrenceRepository.save(scheduleRecurrence);
    }

    public void deleteAllBySchedule(Schedule schedule) {
        scheduleRecurrenceRepository.deleteAllBySchedule(schedule);
        log.info("delete all schedule recurrence");
    }
}
