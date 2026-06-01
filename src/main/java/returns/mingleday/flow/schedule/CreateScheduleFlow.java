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
            if (!start.toLocalDate().equals(end.toLocalDate())) {
                throw new BaseException(ScheduleExceptionCode.ALLOWED_ONLY_SAMEDAY);
            }

            if (request.getIsAllDay()) {
                start = start.toLocalDate().atStartOfDay();
                end = end.toLocalDate().atTime(23, 59, 0);
            }

            // 인스턴스 반복 생성
            List<ScheduleInstance> scheduleInstances = new ArrayList<>();
            if (scheduleRecurrence.getRepeatType() == RepeatType.INTERVAL || scheduleRecurrence.getRepeatType() == RepeatType.DAILY) {
                int interval = (scheduleRecurrence.getRepeatType() == RepeatType.DAILY) ? 1 : Integer.parseInt(scheduleRecurrence.getRepeatValue());

                if (scheduleRecurrence.getEndType() == EndType.COUNT) {
                    int cnt = Math.min(Integer.parseInt(scheduleRecurrence.getEndValue()), 100);
                    for (int i = 0; i < cnt; i++) {
                        scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, start, end));
                        start = start.plusDays(interval);
                        end = end.plusDays(interval);
                    }
                } else {
                    LocalDateTime limit = LocalDateTime.parse(scheduleRecurrence.getEndValue());
                    int cnt = 0;
                    while (cnt < 100 && start.isBefore(limit)) {
                        scheduleInstances.add(scheduleService.createRecurrenceScheduleInstance(schedule, start, end));
                        start = start.plusDays(interval);
                        end = end.plusDays(interval);
                        cnt++;
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

                LocalDateTime limit = scheduleRecurrence.getEndType() == EndType.DATE
                        ? LocalDateTime.parse(scheduleRecurrence.getEndValue())
                        : LocalDateTime.MAX;

                int max = scheduleRecurrence.getEndType() == EndType.COUNT
                        ? Math.min(Integer.parseInt(scheduleRecurrence.getEndValue()), 100)
                        : 100;

                Duration duration = Duration.between(start, end);
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

                        if (candidateStart.isAfter(limit)) {
                            cnt = max;
                            break;
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
                int max = scheduleRecurrence.getEndType() == EndType.COUNT ? Math.min(Integer.parseInt(scheduleRecurrence.getEndValue()), 100) : 100;
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

    @Transactional
    public DetailScheduleResponse createScheduleV2(Integer userId, Integer mingleId, CreateScheduleRequest request) {
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
        if(!schedule.getIsRepeated()) {
            scheduleInstance = createSimpleSchedule(schedule, request.getStartAt(), request.getEndAt(), request.getIsAllDay());
            scheduleInstance2 = null;
        } else {
            // Recurrence부터 생성
            ScheduleRecurrence scheduleRecurrence = scheduleRecurrenceService.createScheduleRecurrence(
                    schedule, request.getRepeatType(), request.getRepeatValue(), request.getEndType(), request.getEndValue()
            );

            LocalDateTime start = request.getStartAt();
            LocalDateTime end = request.getEndAt();
            if (start.isAfter(end)) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }

            if (request.getIsAllDay()) {
                start = start.toLocalDate().atStartOfDay();
                end = start.toLocalDate().atTime(23, 59, 0);
            }


        }

        /*
        * 우선 일정은 단순 일정과 반복 일정으로 나뉨
        * 만약, 단순 일정인 경우
        *   - 그냥 추가하면 됨
        *
        * 만약, 반복 일정인 경우
        *   - 반복 방식으로 케이스 나눔
        *   - INTERVAL 값이 1인 경우 DAILY 알아서 변경
        *       - DAILY
        *           - 매일하는 일정이므로 일정이 00:00부터 24:00을 넘어서면 안 됨
        *       - WEEKLY
        *           - 매주하는 일정을 요일로 설정하려면 일정이 월 00:00 ~ 일 24:00 사이로 설정되어야 함
        *           - 한 주의 범위를 넘어서는 일정이라면 INTERVAL 방식으로 미리 바꾸는 흐름 필요
        *       - MONTHLY
        *           - 매월하는 일정은 일정이 1일 00:00 ~ 말일 24:00 사이로 설정되어야 함
        *           - 한 일정 인스턴스의 범위가 한 달의 범위를 넘어선다면 직접 추가하는 로직 필요
        *       - INTERVAL
        *   - 반복 방식이 무엇이든 최대 Instance 생성은 100회로 제한함
        *
        * 각 케이스별로 메서드 생성
        * ArrayList에 저장된 인스턴스를 일괄 처리하는 공통 로직 메서드 생성
        *  */
        return null;
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
