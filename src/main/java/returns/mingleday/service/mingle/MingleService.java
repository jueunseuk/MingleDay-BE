package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.CreateMingleRequest;
import returns.mingleday.repository.*;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MingleService {

    private final MingleRepository mingleRepository;
    private final ScheduleRepository scheduleRepository;
    private final MingleLogRepository mingleLogRepository;
    private final MingleMemberRepository mingleMemberRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleInstanceRepository scheduleInstanceRepository;
    private final ScheduleRecurrenceRepository scheduleRecurrenceRepository;

    @Transactional
    public Mingle createMingle(User user, CreateMingleRequest request) {
        return createMingle(user, request, "");
    }

    @Transactional
    public Mingle createMingle(User user, CreateMingleRequest request, String profileUrl) {
        Mingle mingle = Mingle.of(
                user, request.getName(),
                request.getDescription(),
                profileUrl,
                request.getUsePermission(),
                request.getUseRealname(),
                request.getMingleType()
        );

        return mingleRepository.save(mingle);
    }

    public Mingle findMingleById(Integer mingleId) {
        return mingleRepository.findById(mingleId)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    public void updateMingle(Mingle mingle, String name, String description, Boolean usePermission, Boolean useRealname) {
        mingle.updateInfo(
                name,
                description,
                usePermission,
                useRealname
        );
    }

    @Transactional
    public void deleteMingleWithAllData(Mingle mingle) {
        List<Schedule> schedules = scheduleRepository.findAlLByMingle(mingle);

        if(!schedules.isEmpty()) {
            scheduleInstanceRepository.deleteAllByScheduleIn(schedules);
            scheduleMemberRepository.deleteAllByScheduleIn(schedules);
            scheduleRecurrenceRepository.deleteAllByScheduleIn(schedules);

            scheduleRepository.deleteAllInBatch(schedules);
        }

        mingleMemberRepository.deleteAllByMingle(mingle);
        mingleLogRepository.deleteAllByMingle(mingle);

        mingleRepository.delete(mingle);
    }
}
