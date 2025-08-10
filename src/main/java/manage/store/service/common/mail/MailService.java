package manage.store.service.common.mail;

import lombok.RequiredArgsConstructor;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.user.value.Email;
import manage.store.model.user.value.OtpNo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${email.from}")
    private String systemEmail;

    @Value("${email.title-prefix}")
    private String mailTitlePrefix;

    private final ResourceLoader resourceLoader;

    private final JavaMailSender mailSender;

    /**
     * Otp 메일 전송
     * @param to 수신자 이메일
     * @param otp 전송할 otp
     * @throws IOException 메일 전송 과정에서 발생한 오류
     */
    public void sendOtpMail(Email to, OtpNo otp) throws IOException {
        if(to == null || otp == null) throw new InvalidParameterException("to email or otp is empty");

        Resource resource = resourceLoader.getResource("classpath:template/otp-mail-message.html");
        String msgTemplate = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String msg = msgTemplate.replace("{otp}", otp.value());

        sendMail(to.value(), "비밀번호 설정 OTP 발송", msg);
    }

    private void sendMail(String to, String subject, String msg) {
        MimeMessagePreparator preparator = (mimeMsg) -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, StandardCharsets.UTF_8.toString());

            helper.setFrom(systemEmail);
            helper.setTo(to);
            helper.setSubject(mailTitlePrefix + subject);
            helper.setText(msg, true);
        };

        mailSender.send(preparator);
    }

}
