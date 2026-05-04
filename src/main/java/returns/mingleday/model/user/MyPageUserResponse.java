package returns.mingleday.model.user;

import lombok.Data;
import returns.mingleday.domain.user.Role;
import returns.mingleday.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MyPageUserResponse {
    private Integer userId;
    private String name;
    private String nickname;
    private String email;
    private String profileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime passwordUpdatedAt;
    private LocalDate birthday;
    private Role role;
    private Integer belongMingleCnt;

    public MyPageUserResponse(User user, Integer belongMingleCnt) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
        this.createdAt = user.getCreatedAt();
        this.passwordUpdatedAt = user.getPasswordUpdatedAt();
        this.birthday = user.getBirthday();
        this.role = user.getRole();
        this.belongMingleCnt = belongMingleCnt;
    }
}
