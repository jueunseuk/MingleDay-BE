package returns.mingleday.domain.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.domain.mingle.MingleMember;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_member")
public class ScheduleMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_member_id")
    private Long scheduleMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mingle_member_id")
    private MingleMember mingleMember;

    @Column(name = "memo")
    private String memo;

    public static ScheduleMember of(Schedule schedule, MingleMember mingleMember, String memo) {
        return ScheduleMember.builder()
                .schedule(schedule)
                .mingleMember(mingleMember)
                .memo(memo)
                .build();
    }
}
