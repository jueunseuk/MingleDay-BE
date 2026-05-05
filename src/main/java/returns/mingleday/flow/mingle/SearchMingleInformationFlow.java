package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.MinglePermission;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.MingleMemberWithPermissionResponse;
import returns.mingleday.model.mingle.MinglePermissionResponse;
import returns.mingleday.model.mingle.MingleResponse;
import returns.mingleday.repository.MinglePermissionRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.user.UserService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchMingleInformationFlow {

    private final UserService userService;
    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;
    private final MinglePermissionRepository minglePermissionRepository;

    public MingleResponse searchMingleInformation(Integer userId, Integer mingleId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember loginMember = mingleMemberService.getMingleMember(mingle, user);

        if (!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        List<MinglePermission> minglePermissions =
                minglePermissionRepository.findAllByMingleOrderByMemberId(mingle);

        Map<MingleMember, List<MinglePermission>> groupedPermissions =
                minglePermissions.stream()
                        .filter(mp -> !mp.getMingleMember().equals(loginMember))
                        .collect(Collectors.groupingBy(
                                MinglePermission::getMingleMember,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        List<MingleMemberWithPermissionResponse> memberResponses =
                groupedPermissions.entrySet()
                        .stream()
                        .map(entry -> {
                            MingleMember member = entry.getKey();

                            List<MinglePermissionResponse> permissions =
                                    entry.getValue()
                                            .stream()
                                            .map(MinglePermissionResponse::new)
                                            .toList();

                            return new MingleMemberWithPermissionResponse(member, permissions);
                        })
                        .toList();

        return new MingleResponse(mingle, memberResponses);
    }
}
