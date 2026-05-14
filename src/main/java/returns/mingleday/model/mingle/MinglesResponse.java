package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleType;

import java.time.LocalDateTime;

@Data
public class MinglesResponse {
    private Integer mingleId;
    private String mingleName;
    private String profileUrl;
    private Integer memberCnt;
    private MingleType mingleType;
    private LocalDateTime createdAt;

    public MinglesResponse(Mingle mingle) {
        this.mingleId = mingle.getMingleId();
        this.mingleName = mingle.getName();
        this.profileUrl = mingle.getProfileUrl();
        this.memberCnt = mingle.getMemberCnt();
        this.mingleType = mingle.getMingleType();
        this.createdAt = mingle.getCreatedAt();
    }
}
