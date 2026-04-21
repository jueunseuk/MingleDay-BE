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
import java.time.format.DateTimeParseException;
import java.util.StringTokenizer;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_recurrence")
public class ScheduleRecurrence extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_recurrence_id")
    private Long scheduleRecurrenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type")
    private RepeatType repeatType;

    @Column(name = "repeat_value")
    private String repeatValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "end_type")
    private EndType endType;

    @Column(name = "end_value")
    private String endValue;

    public static ScheduleRecurrence of(Schedule schedule, RepeatType repeatType, String repeatValue, EndType endType, String endValue) {
        if(repeatType == null || repeatValue == null || endType == null || endValue == null) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        isValidRepeatValue(repeatType, repeatValue);
        isValidEndValue(endType, endValue);

        return ScheduleRecurrence.builder()
                .schedule(schedule)
                .repeatType(repeatType)
                .repeatValue(repeatValue)
                .endType(endType)
                .endValue(endValue)
                .build();
    }

    public void update(RepeatType repeatType, String repeatValue, EndType endType, String endValue) {
        if(repeatType == null || repeatValue == null || endType == null || endValue == null) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        isValidRepeatValue(repeatType, repeatValue);
        isValidEndValue(endType, endValue);

        this.endType = endType;
        this.endValue = endValue;
        this.repeatType = repeatType;
        this.repeatValue = repeatValue;
    }

    public static void isValidRepeatValue(RepeatType repeatType, String value) {
        if(repeatType == RepeatType.INTERVAL) {
            try {
                if(Integer.parseInt(value) < 1) {
                    throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
                }
            } catch (NumberFormatException e) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        } else if(repeatType == RepeatType.WEEKLY) {
            StringTokenizer st = new StringTokenizer(value, ",");
            try {
                while(st.hasMoreTokens()) {
                    int curr = Integer.parseInt(st.nextToken());
                    if(curr < 0 || curr > 6) {
                        throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
                    }
                }
            } catch (NumberFormatException e) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        } else if(repeatType == RepeatType.MONTHLY) {
            StringTokenizer st = new StringTokenizer(value, ",");
            try {
                while(st.hasMoreTokens()) {
                    int curr = Integer.parseInt(st.nextToken());
                    if(curr < 0 || curr > 31) {
                        throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
                    }
                }
            } catch (NumberFormatException e) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }
    }

    public static void isValidEndValue(EndType endType, String value) {
        if(endType == EndType.DATE) {
            try {
                if(LocalDateTime.parse(value).isAfter(LocalDateTime.now().plusYears(1))) {
                    throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
                }
            } catch (DateTimeParseException e) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        } else {
            try {
                if(Integer.parseInt(value) < 1 || Integer.parseInt(value) > 100) {
                    throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
                }
            } catch (NumberFormatException e) {
                throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }
    }
}