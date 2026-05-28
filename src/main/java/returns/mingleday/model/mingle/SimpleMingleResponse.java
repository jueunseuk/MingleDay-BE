package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.Mingle;

@Data
public class SimpleMingleResponse {
    private Integer mingleId;
    private String mingleName;

    public SimpleMingleResponse(Mingle mingle) {
        this.mingleId = mingle.getMingleId();
        this.mingleName = mingle.getName();
    }
}
