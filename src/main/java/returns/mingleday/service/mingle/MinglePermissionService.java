package returns.mingleday.service.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.MinglePermission;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.repository.MinglePermissionRepository;
import returns.mingleday.response.code.MinglePermissionExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinglePermissionService {

    private final MinglePermissionRepository minglePermissionRepository;

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
}
