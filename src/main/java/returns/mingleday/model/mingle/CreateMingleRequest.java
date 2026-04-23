package returns.mingleday.model.mingle;

import lombok.Data;
import returns.mingleday.domain.mingle.MingleType;

@Data
public class CreateMingleRequest {
    private String name;
    private String description;
    private Boolean usePermission;
    private Boolean useRealname;
    private MingleType mingleType;
}
