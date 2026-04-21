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
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.mingle.log.strategy.CreateLogStrategy;
import returns.mingleday.service.schedule.ScheduleMemberService;
import returns.mingleday.service.schedule.ScheduleRecurrenceService;
import returns.mingleday.service.schedule.ScheduleService;
import returns.mingleday.service.user.UserService;

import java.time.DayOfWeek;
import java.time.Duration;
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
    private final MinglePermissionService minglePermissionService;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleRecurrenceService scheduleRecurrenceService;
    private final ScheduleInstanceRepository scheduleInstanceRepository;
    private final CreateLogStrategy createLogStrategy;
    private final CreateMingleLogService createMingleLogService;

    @Transactional
    public DetailScheduleResponse createSchedule(Integer userId, Integer mingleId, CreateScheduleRequest request) {
        User user = userService.findUserByUserId(userId);

        if(!request.getMingleId().equals(mingleId)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }
        Mingle mingle = mingleService.findMingleById(mingleId);

        // 등록 권한 확인
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() && !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.CREATE)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        // 스케줄 생성
        Schedule schedule = scheduleService.createSchedule(mingle, user, request);

        // 스케줄 해당 멤버 등록
        List<ScheduleMember> scheduleMembers = new ArrayList<>();
        for (ScheduleMemberRequest member : request.getMingleMembers()) {
            MingleMember m = mingleMemberService.getMingleMember(member.getScheduleMemberId());
            scheduleMembers.add(scheduleMemberService.createScheduleMember(schedule, m, member.getMemo()));
        }
        scheduleMemberRepository.saveAll(scheduleMembers);

        // 스케줄 멤버 DTO로 변환
        List<ScheduleMemberResponse> scheduleMemberResponses = scheduleMembers.stream().map(ScheduleMemberResponse::new).toList();

        // 스케줄 인스턴스 생성
        ScheduleInstance scheduleInstance, scheduleInstance2;
        if(schedule.getIsRepeated()) {
            // Recurrence부터 생성
            ScheduleRecurrence scheduleRecurrence = scheduleRecurrenceService.createScheduleRecurrence(
                    schedule, request.getRepeatType(), request.getRepeatValue(), request.getEndType(), request.getEndValue()
            );

            LocalDateTime start = request.getStartAt();
            LocalDateTime end = request.getEndAt();
            if(start.isAfter(end)) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
            if(start.getDayOfYear() != end.getDayOfYear()) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }

            // 인스턴스 반복 생성
            List<ScheduleInstance> scheduleInstances = new ArrayList<>();
            if(scheduleRecurrence.getRepeatType() == RepeatType.INTERVAL || scheduleRecurrence.getRepeatType() == RepeatType.DAILY) {
                int interval = (scheduleRecurrence.getRepeatType() == RepeatType.DAILY) ? 1
                        : Integer.parseInt(scheduleRecurrence.getRepeatValue());
                if(scheduleRecurrence.getEndType() == EndType.COUNT) {
                    int cnt = Integer.parseInt(scheduleRecurrence.getEndValue());
                    for(int i = 0; i < cnt; i++) {
                        scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, start, end));
                        start = start.plusDays(interval);
                        end = end.plusDays(interval);
                    }
                } else {
                    LocalDateTime limit = LocalDateTime.parse(scheduleRecurrence.getEndValue());
                    while(start.isBefore(limit)) {
                        scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, start, end));
                        start = start.plusDays(interval);
                        end = end.plusDays(interval);
                    }
                }
            } else if (scheduleRecurrence.getRepeatType() == RepeatType.WEEKLY) {
                List<DayOfWeek> targetDays = Arrays.stream(scheduleRecurrence.getRepeatValue().split(","))
                        .map(s -> {
                            int val = Integer.parseInt(s.trim());
                            return DayOfWeek.of(val == 0 ? 7 : val);
                        })
                        .sorted()
                        .toList();

                int max = (scheduleRecurrence.getEndType() == EndType.COUNT) ? Integer.parseInt(scheduleRecurrence.getEndValue()) : Integer.MAX_VALUE;
                LocalDateTime limit = (scheduleRecurrence.getEndType() == EndType.DATE) ? LocalDateTime.parse(scheduleRecurrence.getEndValue()) : LocalDateTime.MAX;

                int cnt = 0;
                LocalDateTime current = start;
                Duration duration = Duration.between(start, end);

                while (cnt < max && !current.isAfter(limit)) {
                    for (DayOfWeek day : targetDays) {
                        LocalDateTime nextOccurrence = current.with(TemporalAdjusters.nextOrSame(day));

                        if (nextOccurrence.isBefore(start)) continue;

                        if (nextOccurrence.isAfter(limit)) {
                            cnt = max;
                            break;
                        }

                        scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, nextOccurrence, nextOccurrence.plus(duration)));

                        if(++cnt >= max) break;
                    }
                    current = current.plusWeeks(1).with(DayOfWeek.MONDAY).withHour(0).withMinute(0);
                }
            } else if (scheduleRecurrence.getRepeatType() == RepeatType.MONTHLY) {
                int max = (scheduleRecurrence.getEndType() == EndType.COUNT) ? Integer.parseInt(scheduleRecurrence.getEndValue()) : Integer.MAX_VALUE;
                LocalDateTime limit = (scheduleRecurrence.getEndType() == EndType.DATE) ? LocalDateTime.parse(scheduleRecurrence.getEndValue()) : LocalDateTime.MAX;

                int cnt = 0;
                LocalDateTime currentStart = start;
                LocalDateTime currentEnd = end;

                while (cnt < max && !currentStart.isAfter(limit)) {
                    scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, currentStart, currentEnd));

                    currentStart = currentStart.plusMonths(1);
                    currentEnd = currentEnd.plusMonths(1);
                    cnt++;
                }
            }

            // 반복 인스턴스 링킹
            if (scheduleInstances.size() > 1) {
                scheduleInstances.get(0).linking(null, scheduleInstances.get(1));
                for (int i = 1; i < scheduleInstances.size() - 1; i++) {
                    scheduleInstances.get(i).linking(scheduleInstances.get(i - 1), scheduleInstances.get(i + 1));
                }
                scheduleInstances.getLast().linking(scheduleInstances.get(scheduleInstances.size() - 2), null);
            } else {
                scheduleInstances.getFirst().linking(null, null);
            }

            scheduleInstanceRepository.saveAll(scheduleInstances);

            // 반환용 인스턴스
            scheduleInstance = scheduleInstances.getFirst();
            if(scheduleInstances.size() > 1) {
                scheduleInstance2 = scheduleInstances.get(1);
            } else {
                scheduleInstance2 = null;
            }
        } else {
            LocalDateTime start = request.getStartAt();
            LocalDateTime end = request.getEndAt();
            if(start.isAfter(end)) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }

            scheduleInstance = scheduleService.createSolidScheduleInstance(schedule, start, end);
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
}
