package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.CreateMingleRequest;
import returns.mingleday.repository.MingleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MingleService {

    private final MingleRepository mingleRepository;

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
}
