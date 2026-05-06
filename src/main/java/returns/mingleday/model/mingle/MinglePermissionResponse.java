package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MinglePermission;
import returns.mingleday.domain.mingle.PermissionType;

@Data
public class MinglePermissionResponse {
    private PermissionType permissionType;
    private Boolean value;

    public MinglePermissionResponse(MinglePermission permission) {
        this.permissionType = permission.getPermissionType();
        this.value = permission.getValue();
    }
}
