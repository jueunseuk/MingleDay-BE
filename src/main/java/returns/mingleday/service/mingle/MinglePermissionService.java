package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.MinglePermission;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.mingle.MingleMemberPermissionResponse;
import returns.mingleday.repository.MinglePermissionRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.MinglePermissionExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.user.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinglePermissionService {

    private final MinglePermissionRepository minglePermissionRepository;
    private final UserService userService;
    private final MingleService mingleService;
    private final MingleMemberService mingleMemberService;

    @Transactional
    public void createFullPermissions(MingleMember mingleMember, Boolean value) {
        minglePermissionRepository.saveAll(Arrays.stream(PermissionType.values())
                                            .map(type -> MinglePermission.builder()
                                                    .mingleMember(mingleMember)
                                                    .permissionType(type)
                                                    .value(value)
                                                    .build())
                                            .toList());
    }

    public MinglePermission findByMingleMemberAndPermissionType(MingleMember mingleMember, PermissionType permissionType) {
        return minglePermissionRepository.findByMingleMemberAndPermissionType(mingleMember, permissionType);
    }

    @Transactional
    public void updateMinglePermissions(MingleMember mingleMember, PermissionType permissionType, Boolean value) {
        MinglePermission minglePermission = findByMingleMemberAndPermissionType(mingleMember, permissionType);

        if(!mingleMember.getMingle().getUsePermission()) {
            throw new BaseException(MinglePermissionExceptionCode.DISABLED_THE_PERMISSION_FUNCTION);
        }

        minglePermission.updateValue(value);
        log.info("Update Mingle Member Permission - mingleMemberId: {}, permissionType: {}", mingleMember.getMingleMemberId(), permissionType);
    }

    public Boolean doesMemberHavePermission(MingleMember mingleMember, PermissionType permissionType) {
        return minglePermissionRepository.findByMingleMemberAndPermissionType(mingleMember, permissionType).getValue();
    }

    public List<MingleMemberPermissionResponse> getMemberPermissions(Integer userId, Integer mingleId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        List<MinglePermission> permissions = minglePermissionRepository.findAllByMingleOrderByMemberId(mingle);

        return permissions.stream()
                .collect(Collectors.groupingBy(MinglePermission::getMingleMember))
                .entrySet()
                .stream()
                .map(entry -> {
                    MingleMember member = entry.getKey();

                    Map<PermissionType, Boolean> permissionMap =
                            entry.getValue()
                                    .stream()
                                    .collect(Collectors.toMap(
                                            MinglePermission::getPermissionType,
                                            MinglePermission::getValue
                                    ));

                    return new MingleMemberPermissionResponse(
                            member.getMingleMemberId(),
                            member.getUser().getUserId(),
                            member.getDisplayName(),
                            permissionMap
                    );
                })
                .toList();
    }

    public MingleMemberPermissionResponse getMemberPermission(
            Integer userId,
            Integer mingleId,
            Long mingleMemberId
    ) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if (!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        MingleMember targetMember = mingleMemberService.getMingleMember(mingleMemberId);

        if (!targetMember.getMingle().equals(mingle)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        List<MinglePermission> permissions = minglePermissionRepository.findAllByMingleMemberOrderByPermissionType(targetMember);

        Map<PermissionType, Boolean> permissionMap =
                permissions.stream()
                        .collect(Collectors.toMap(
                                MinglePermission::getPermissionType,
                                MinglePermission::getValue
                        ));

        return new MingleMemberPermissionResponse(
                targetMember.getMingleMemberId(),
                targetMember.getUser().getUserId(),
                targetMember.getDisplayName(),
                permissionMap
        );
    }
}
