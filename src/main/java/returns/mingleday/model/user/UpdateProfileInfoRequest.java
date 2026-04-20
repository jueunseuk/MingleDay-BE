package returns.mingleday.model.user;

import lombok.Data;

@Data
public class UpdateProfileInfoRequest {
    private Integer userId;
    private String name;
    private String nickname;
}
