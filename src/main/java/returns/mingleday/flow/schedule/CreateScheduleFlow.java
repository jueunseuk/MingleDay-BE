package returns.mingleday.flow.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.schedule.*;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.schedule.*;
import returns.mingleday.repository.ScheduleInstanceRepository;
import returns.mingleday.repository.ScheduleMemberRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.ScheduleExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.schedule.ScheduleMemberService;
import returns.mingleday.service.schedule.ScheduleRecurrenceService;
import returns.mingleday.service.schedule.ScheduleService;
import returns.mingleday.service.user.UserService;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateScheduleFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final ScheduleService scheduleService;
    private final MingleMemberService mingleMemberService;
    private final ScheduleMemberService scheduleMemberService;
    private final CreateMingleLogService createMingleLogService;
    private final MinglePermissionService minglePermissionService;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleRecurrenceService scheduleRecurrenceService;
    private final ScheduleInstanceRepository scheduleInstanceRepository;

    @Transactional
    public DetailScheduleResponse createSchedule(Integer userId, Integer mingleId, CreateScheduleRequest request) {
        User user = userService.findUserByUserId(userId);

        if (!request.getMingleId().equals(mingleId)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }
        Mingle mingle = mingleService.findMingleById(mingleId);

        // 등록 권한 확인
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if (mingle.getUsePermission() && !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.CREATE)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        // 스케줄 생성
        Schedule schedule = scheduleService.createSchedule(mingle, user, request);

        // 스케줄 해당 멤버 등록
        List<ScheduleMember> scheduleMembers = assignScheduleMember(schedule, request.getMingleMembers());

        // 스케줄 멤버 DTO로 변환
        List<ScheduleMemberResponse> scheduleMemberResponses = scheduleMembers.stream().map(ScheduleMemberResponse::new).toList();

        // 스케줄 인스턴스 생성
        ScheduleInstance scheduleInstance, scheduleInstance2;
        if (schedule.getIsRepeated()) {
            // Recurrence부터 생성
            ScheduleRecurrence scheduleRecurrence = scheduleRecurrenceService.createScheduleRecurrence(
                    schedule, request.getRepeatType(), request.getRepeatValue(), request.getEndType(), request.getEndValue()
            );

            LocalDateTime start = request.getStartAt();
            LocalDateTime end = request.getEndAt();
            if (start.isAfter(end)) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
            if (scheduleRecurrence.getRepeatType() != RepeatType.INTERVAL && !start.toLocalDate().equals(end.toLocalDate())) {
                throw new BaseException(ScheduleExceptionCode.ALLOWED_ONLY_SAMEDAY);
            }

            if (request.getIsAllDay()) {
                start = start.toLocalDate().atStartOfDay();
                end = end.toLocalDate().atTime(23, 59, 0);
            }

            // 인스턴스 반복 생성
            List<ScheduleInstance> scheduleInstances = new ArrayList<>();
            int max = Math.min(Integer.parseInt(scheduleRecurrence.getEndValue()), 100);
            Duration duration = Duration.between(start, end);
            if (scheduleRecurrence.getRepeatType() == RepeatType.DAILY) {
                for (int i = 0; i < max; i++) {
                    scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, start, end));
                    start = start.plusDays(1);
                    end = end.plusDays(1);
                }

            } else if (scheduleRecurrence.getRepeatType() == RepeatType.WEEKLY) {
                List<DayOfWeek> targetDays = Arrays.stream(scheduleRecurrence.getRepeatValue().split(","))
                        .map(s -> {
                            int val = Integer.parseInt(s.trim());
                            return DayOfWeek.of(val == 0 ? 7 : val);
                        })
                        .sorted()
                        .toList();

                LocalDate weekStartDate = start.toLocalDate()
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                int cnt = 0;

                while (cnt < max) {
                    for (DayOfWeek target : targetDays) {
                        LocalDate candidateDate = weekStartDate.with(TemporalAdjusters.nextOrSame(target));
                        LocalDateTime candidateStart = LocalDateTime.of(candidateDate, start.toLocalTime());

                        if (candidateStart.isBefore(start)) {
                            continue;
                        }

                        LocalDateTime candidateEnd = candidateStart.plus(duration);
                        scheduleInstances.add(
                                scheduleService.createRecurrenceScheduleInstance(schedule, candidateStart, candidateEnd)
                        );

                        cnt++;
                        if (cnt >= max) {
                            break;
                        }
                    }

                    weekStartDate = weekStartDate.plusWeeks(1);
                }
            } else if (scheduleRecurrence.getRepeatType() == RepeatType.MONTHLY) {
                LocalDateTime currentStart = start;
                LocalDateTime currentEnd = end;

                for (int i = 0; i < max; i++) {
                    scheduleInstances.add(
                            scheduleService.createRecurrenceScheduleInstance(schedule, currentStart, currentEnd)
                    );

                    currentStart = currentStart.plusMonths(1);
                    currentEnd = currentEnd.plusMonths(1);
                }

            } else if (scheduleRecurrence.getRepeatType() == RepeatType.INTERVAL) {
                int interval = Integer.parseInt(scheduleRecurrence.getRepeatValue());

                LocalDateTime currentStart = start;
                LocalDateTime currentEnd = end;

                for (int i = 0; i < max; i++) {
                    scheduleInstances.add(
                            scheduleService.createRecurrenceScheduleInstance(schedule, currentStart, currentEnd)
                    );

                    currentStart = currentStart.plusDays(interval + 1L);
                    currentEnd = currentEnd.plusDays(interval + 1L);
                }
            }

            // 반복 인스턴스 링킹
            if (scheduleInstances.size() > 1) {
                scheduleInstances.get(0).linking(null, scheduleInstances.get(1));
                for (int i = 1; i < scheduleInstances.size() - 1; i++) {
                    scheduleInstances.get(i).linking(scheduleInstances.get(i - 1), scheduleInstances.get(i + 1));
                }
                scheduleInstances.get(scheduleInstances.size() - 1).linking(scheduleInstances.get(scheduleInstances.size() - 2), null);
            } else {
                scheduleInstances.get(0).linking(null, null);
            }

            scheduleInstanceRepository.saveAll(scheduleInstances);

            // 반환용 인스턴스
            scheduleInstance = scheduleInstances.get(0);
            if (scheduleInstances.size() > 1) {
                scheduleInstance2 = scheduleInstances.get(1);
            } else {
                scheduleInstance2 = null;
            }
        } else { // isRepeated is false
            scheduleInstance = createSimpleSchedule(schedule, request.getStartAt(), request.getEndAt(), request.getIsAllDay());
            scheduleInstance2 = null;
        }

        createMingleLogService.execute(mingle, mingleMember, schedule, MingleLogType.CREATE);
        log.info("Create schedule - userId: {}, scheduleId: {}", userId, schedule.getScheduleId());
        return new DetailScheduleResponse(
                schedule,
                scheduleMemberResponses,
                new ScheduleInstanceResponse(scheduleInstance, null, new SimpleScheduleInstanceResponse(scheduleInstance2))
        );
    }

    private List<ScheduleMember> assignScheduleMember(Schedule schedule, List<ScheduleMemberRequest> members) {
        List<ScheduleMember> scheduleMembers = new ArrayList<>();
        for (ScheduleMemberRequest member : members) {
            MingleMember m = mingleMemberService.getMingleMember(member.getMingleMemberId());
            scheduleMembers.add(scheduleMemberService.createScheduleMember(schedule, m, member.getMemo()));
        }
        return scheduleMemberRepository.saveAll(scheduleMembers);
    }

    private ScheduleInstance createSimpleSchedule(Schedule schedule, LocalDateTime start, LocalDateTime end, Boolean isAllDay) {
        if (start.isAfter(end)) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        if (isAllDay) {
            start = start.toLocalDate().atStartOfDay();
            end = end.toLocalDate().atTime(23, 59, 0);
        }

        return scheduleService.createSolidScheduleInstance(schedule, start, end);
    }
}
