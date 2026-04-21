package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.Mingle;

@Data
public class MinglesResponse {
    private Integer mingleId;
    private String mingleName;
    private String profileUrl;

    public MinglesResponse(Mingle mingle) {
        this.mingleId = mingle.getMingleId();
        this.mingleName = mingle.getName();
        this.profileUrl = mingle.getProfileUrl();
    }
}
