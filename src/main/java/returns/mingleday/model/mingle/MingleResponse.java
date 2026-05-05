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
    private List<MingleMemberWithPermissionResponse> mingleMEmbers;

    public MingleResponse(Mingle mingle, List<MingleMemberWithPermissionResponse> mingleMEmbers) {
        this.mingleId = mingle.getMingleId();
        this.mingleName = mingle.getName();
        this.profileUrl = mingle.getProfileUrl();
        this.mingleType = mingle.getMingleType();
        this.createdAt = mingle.getCreatedAt();
        this.mingleMEmbers = mingleMEmbers;
    }
}
