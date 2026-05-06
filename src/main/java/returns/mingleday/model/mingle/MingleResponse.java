package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MingleResponse {
    private Integer mingleId;
    private String mingleName;
    private String profileUrl;
    private MingleType mingleType;
    private LocalDateTime createdAt;
    private String ownerName;
    private Boolean useRealname;
    private Boolean usePermission;
    private List<MingleMemberWithPermissionResponse> mingleMembers;

    public MingleResponse(Mingle mingle, List<MingleMemberWithPermissionResponse> mingleMembers) {
        this.mingleId = mingle.getMingleId();
        this.mingleName = mingle.getName();
        this.profileUrl = mingle.getProfileUrl();
        this.mingleType = mingle.getMingleType();
        this.createdAt = mingle.getCreatedAt();
        this.ownerName = mingle.getOwner().getName(); // only showing real name
        this.useRealname = mingle.getUseRealname();
        this.usePermission = mingle.getUsePermission();
        this.mingleMembers = mingleMembers;
    }
}
