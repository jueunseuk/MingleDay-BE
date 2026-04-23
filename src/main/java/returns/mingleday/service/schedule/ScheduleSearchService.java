package returns.mingleday.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.schedule.ScheduleInstance;
import returns.mingleday.domain.schedule.ScheduleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.*;
import returns.mingleday.repository.ScheduleInstanceRepository;
import returns.mingleday.repository.ScheduleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleSearchService {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleRepository scheduleRepository;
    private final MingleMemberService mingleMemberService;
    private final ScheduleMemberService scheduleMemberService;
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

    public List<MonthlyScheduleResponse> findMonthlySchedules(Integer userId, Integer mingleId, Integer year, Integer month) {
        User user = userService.findUserByUserId(userId);
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        int lastDay = LocalDate.of(year, month, 1).lengthOfMonth();
        LocalDateTime end = LocalDateTime.of(year, month, lastDay, 23, 59);

        Mingle mingle = mingleService.findMingleById(mingleId);
        mingleMemberService.getMingleMember(mingle, user);

        List<ScheduleInstance> scheduleInstances = scheduleInstanceRepository.findAllByMingleAndOneMonth(mingle, user, start, end);
        return scheduleInstances.stream().map(si ->
           new MonthlyScheduleResponse(
                   si.getSchedule(),
                   new SimpleScheduleInstanceResponse(si)
               )
        ).toList();
    }

    public List<DailyScheduleResponse> findDailySchedules(Integer userId, Integer mingleId, Integer year, Integer month, Integer day) {
        User user = userService.findUserByUserId(userId);
        LocalDateTime start = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, month, day).atTime(23, 59, 0);

        Mingle mingle = mingleService.findMingleById(mingleId);
        mingleMemberService.getMingleMember(mingle, user);

        List<ScheduleInstance> scheduleInstances = scheduleInstanceRepository.findAllByMingleAndOneMonth(mingle, user, start, end);
        return scheduleInstances.stream().map(si ->
                new DailyScheduleResponse(
                        si.getSchedule(),
                        new SimpleScheduleInstanceResponse(si)
                )
        ).toList();
    }

    public List<MonthlyScheduleResponse> findMySchedules(Integer userId, Integer year, Integer month, String keyword) {
        User user = userService.findUserByUserId(userId);
        LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, month, 1).atTime(23, 59, 0);

        List<ScheduleInstance> scheduleInstances;
        if(keyword == null || keyword.isEmpty()) {
            scheduleInstances = scheduleInstanceRepository.findAllByUserAndMonth(user, start, end);
        } else {
            scheduleInstances = scheduleInstanceRepository.findAllByUserAndMonthAndTitle(user, start, end, keyword);
        }

        return scheduleInstances.stream().map(si -> new MonthlyScheduleResponse(
                si.getSchedule(),
                new SimpleScheduleInstanceResponse(si)
        )).toList();
    }

    public DetailScheduleResponse getDetailSchedules(Integer userId, Integer mingleId, Long scheduleInstanceId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        mingleMemberService.getMingleMember(mingle, user);
        ScheduleInstance scheduleInstance = findScheduleInstanceById(scheduleInstanceId);
        Schedule schedule = scheduleInstance.getSchedule();

        if(!schedule.getMingle().equals(mingle)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }

        List<ScheduleMember> scheduleMembers = scheduleMemberService.findScheduleMemberBySchedule(schedule);

        return new DetailScheduleResponse(
                schedule,
                scheduleMembers.stream().map(ScheduleMemberResponse::new).toList(),
                new ScheduleInstanceResponse(
                        scheduleInstance,
                        new SimpleScheduleInstanceResponse(scheduleInstance.getPrevScheduleInstance()),
                        new SimpleScheduleInstanceResponse(scheduleInstance.getNextScheduleInstance())
                )
        );
    }
}
