package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.schedule.ScheduleStatus;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.CreateScheduleRequest;
import returns.mingleday.repository.ScheduleInstanceRepository;
import returns.mingleday.repository.ScheduleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.category.CategoryService;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final UserService userService;
    private final MingleService mingleService;
    private final CategoryService categoryService;
    private final ScheduleRepository scheduleRepository;
    private final MingleMemberService mingleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final MinglePermissionService minglePermissionService;
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

    @Transactional
    public void updateStatus(Integer userId, Integer mingleId, Long scheduleId, Long scheduleInstanceId, ScheduleStatus status) {
        User user = userService.findUserByUserId(userId);

        Mingle mingle = mingleService.findMingleById(mingleId);

        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() && !mingle.getOwner().equals(user) && !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.MODIFY)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        ScheduleInstance scheduleInstance = scheduleSearchService.findScheduleInstanceById(scheduleInstanceId);
        if(!scheduleInstance.getSchedule().getMingle().equals(mingle)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }

        scheduleInstance.updateStatus(status);
    }

    @Transactional
    public void updateSchedule(Schedule schedule, String title, String content, String location, Category category, Boolean isLocked, Boolean isPrivate) {
        schedule.update(
                title != null ? title : schedule.getTitle(),
                content != null ? content : schedule.getContent(),
                location != null ? location : schedule.getLocation(),
                category != null ? category : schedule.getCategory(),
                isLocked != null ? isLocked : schedule.getIsLocked(),
                isPrivate != null ? isPrivate : schedule.getIsPrivate()
        );
    }

    @Transactional
    public void updateScheduleInstance(ScheduleInstance scheduleInstance, LocalDateTime startAt, LocalDateTime endAt, String memo) {
        if(scheduleInstance.getPrevScheduleInstance().getEndAt().isBefore(startAt) ||
                scheduleInstance.getNextScheduleInstance().getStartAt().isAfter(endAt)) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        scheduleInstance.update(
                startAt != null ? startAt : scheduleInstance.getStartAt(),
                endAt != null ? endAt : scheduleInstance.getEndAt(),
                memo != null ? memo : scheduleInstance.getMemo()
        );
    }

    public void deleteAllBySchedule(Schedule schedule) {
        List<ScheduleInstance> instances = scheduleInstanceRepository.findAllBySchedule(schedule);

        for (ScheduleInstance instance : instances) {
            instance.linking(null, null);
        }

        scheduleInstanceRepository.flush();
        scheduleInstanceRepository.deleteAllBySchedule(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        scheduleRepository.delete(schedule);
        log.info("delete all schedule instance");
    }
}
