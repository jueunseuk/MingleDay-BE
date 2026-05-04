package returns.mingleday.model.mingle;

import lombok.AllArgsConstructor;
import lombok.Data;
import returns.mingleday.domain.mingle.PermissionType;

@Data
@AllArgsConstructor
public class MinglePermissionRequest {
    private PermissionType permission;
    private Boolean value;
}
