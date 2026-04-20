package returns.mingleday.flow.mingle;

import lombok.Data;

@Data
public class MingleMemberResponse {
    private Integer userId;
    private Long memberId;
    private String memberName;
    // 자기랑 같이 소속된 밍글도 보여주기
}
