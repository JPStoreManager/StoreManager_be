package manage.store.dto.user.find;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPwBaseResponse{

    private final String sessionId;

    /**
     * @param sessionId 성공 시 발급되는 세션 아이디
     *                  실패 시 null
     */
    public FindPwBaseResponse(String sessionId) {
        this.sessionId = sessionId;
    }
}
