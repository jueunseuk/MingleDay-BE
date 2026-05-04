package returns.mingleday.model.user;

import lombok.Data;
import returns.mingleday.domain.user.User;

import java.time.LocalDateTime;

@Data
public class SimpleUserResponse {
    private Integer userId;
    private String name;
    private String profileUrl;
    private LocalDateTime createdAt;

    public SimpleUserResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.profileUrl = user.getProfileUrl();
        this.createdAt = user.getCreatedAt();
    }
}
