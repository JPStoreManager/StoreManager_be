package manage.store.dto.user.find;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.user.value.Email;
import manage.store.model.user.value.UserId;

@Getter @Setter
@ToString
public class FindUserPwSession {

    private final Step[] stepOrder = {Step.NONE, Step.SEND_OTP, Step.VALIDATE_OTP, Step.NEW_PW, Step.END};

    private Step completedStep;
    private UserId userId;
    private Email userEmail;

    public FindUserPwSession(Step completedStep, UserId userId, Email userEmail) {
        if(completedStep == null || userId == null || userEmail == null)
            throw new InvalidParameterException("The parameter is invalid. " + completedStep + ", " + userId + ", " + userEmail);

        this.completedStep = completedStep;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    /**
     * 다음 비밀번호 찾기의 Step을 조회한다.
     * @return NONE - 아무 Step도 진행되지 않았을 경우 <br>
     * END - 모든 비밀번호 찾기 시나리오가 종료되었을 때
     */
    public Step getNextStep() {
        if(completedStep.equals(Step.END)) return Step.END;

        for (int i = 0; i < stepOrder.length; i++) {
            if(stepOrder[i].equals(completedStep)) {
                return stepOrder[i + 1];
            }
        }
        return Step.NONE;
    }

    public enum Step {
        NONE("none", 0),
        SEND_OTP("sendOtp", 1),
        VALIDATE_OTP("validateOtp", 2),
        NEW_PW("newPassword", 3),
        END("end", 4);

        private final String step;
        private final int stepOrder;

        Step(String step, int stepOrder) {
            this.step = step;
            this.stepOrder = stepOrder;
        }

        public String getStep(){
            return this.step;
        }

        public int getStepOrder(){
            return this.stepOrder;
        }
    }
}
