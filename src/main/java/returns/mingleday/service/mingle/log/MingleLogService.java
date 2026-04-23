package returns.mingleday.service.mingle.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLog;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.MingleLogResponse;
import returns.mingleday.model.mingle.MyMingleLogResponse;
import returns.mingleday.repository.MingleLogRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;
import returns.mingleday.util.PageableMaker;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MingleLogService {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;
    private final MingleLogRepository mingleLogRepository;

    public List<MyMingleLogResponse> getAllMyMingleLog(Integer userId) {
        User user = userService.findUserByUserId(userId);

        List<Mingle> mingles = mingleMemberService.getAllMingle(user);
        List<MingleLog> mingleLogs = mingleLogRepository.findAllByMingleInOrderByCreatedAt(mingles, PageableMaker.of("createdAt"));

        return mingleLogs.stream().map(MyMingleLogResponse::new).toList();
    }

    public List<MingleLogResponse> getAllMingleLog(Integer userId, Integer mingleId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(mingleMemberService.getMingleMember(mingle, user) == null) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        List<MingleLog> mingleLogs = mingleLogRepository.findAllByMingleOrderByCreatedAt(mingle, PageableMaker.of("createdAt"));

        return mingleLogs.stream().map(MingleLogResponse::new).toList();
    }
}
