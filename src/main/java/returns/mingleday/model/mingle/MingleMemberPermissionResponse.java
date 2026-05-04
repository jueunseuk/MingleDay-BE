package returns.mingleday.model.mingle;

import lombok.AllArgsConstructor;
import lombok.Data;
import returns.mingleday.domain.mingle.PermissionType;

import java.util.Map;

@Data
@AllArgsConstructor
public class MingleMemberPermissionResponse {
    private Long mingleMemberId;
    private Integer userId;
    private String nickname;
    private Map<PermissionType, Boolean> permissions;
}