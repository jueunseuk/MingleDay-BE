package returns.mingleday.domain.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_instance")
public class ScheduleInstance extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_instance_id")
    private Long scheduleInstanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "memo")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status")
    private ScheduleStatus scheduleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_schedule_instance_id")
    private ScheduleInstance prevScheduleInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_schedule_instance_id")
    private ScheduleInstance nextScheduleInstance;

    public static ScheduleInstance of(Schedule schedule, LocalDateTime startAt, LocalDateTime endAt, String memo) {
        long daysBetween = ChronoUnit.DAYS.between(startAt, endAt);
        if (daysBetween > 100) {
            throw new BaseException(GlobalExceptionCode.INVALID_REQUEST);
        }

        return ScheduleInstance.builder()
                .schedule(schedule)
                .startAt(startAt)
                .endAt(endAt)
                .memo(memo)
                .scheduleStatus(endAt.isBefore(LocalDateTime.now()) ? ScheduleStatus.COMPLETED : ScheduleStatus.TODO)
                .prevScheduleInstance(null)
                .nextScheduleInstance(null)
                .build();
    }

    public void linking(ScheduleInstance prevScheduleInstance, ScheduleInstance nextScheduleInstance) {
        this.prevScheduleInstance = prevScheduleInstance;
        this.nextScheduleInstance = nextScheduleInstance;
    }

    public void updateStatus(ScheduleStatus status) {
        this.scheduleStatus = status;
    }

    public void update(LocalDateTime startAt, LocalDateTime endAt, String memo) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.memo = memo;
    }
}
