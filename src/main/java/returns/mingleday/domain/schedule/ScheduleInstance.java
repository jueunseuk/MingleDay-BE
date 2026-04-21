package returns.mingleday.domain.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;

import java.time.LocalDate;

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
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "memo")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status")
    private ScheduleStatus scheduleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_schedule_instance_id")
    private ScheduleInstance prevScheduleInstanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_schedule_instance_id")
    private ScheduleInstance nextScheduleInstanceId;
}
