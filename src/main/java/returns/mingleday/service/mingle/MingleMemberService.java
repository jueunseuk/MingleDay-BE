package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.MingleMemberRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MingleMemberService {

    private final MingleMemberRepository mingleMemberRepository;

    @Transactional
    public MingleMember createMingleMember(Mingle mingle, User member) {
        MingleMember mingleMember = MingleMember.of(mingle, member);
        return mingleMemberRepository.save(mingleMember);
    }

    public MingleMember getMingleMember(Long mingleMemberId) {
        return mingleMemberRepository.findById(mingleMemberId)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    public MingleMember getMingleMember(Mingle mingle, User user) {
        return mingleMemberRepository.findByMingleAndUser(mingle, user)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }
}
