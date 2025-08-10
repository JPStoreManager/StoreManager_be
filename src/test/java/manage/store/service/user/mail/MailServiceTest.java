package manage.store.service.user.mail;

import manage.store.consts.Tags;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.user.value.Email;
import manage.store.model.user.value.OtpNo;
import manage.store.service.common.mail.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("sendOtpMail 성공 (No exception)")
    void sendOtpMail_success() throws IOException {
        // Given
        final Email email = new Email("chickenman10@naver.com");
        final OtpNo otp = new OtpNo("123456");

        doNothing().when(mailSender).send(any(MimeMessagePreparator.class));
        given(resourceLoader.getResource(any()))
                .willReturn(new ClassPathResource("template/otp-mail-message.html"));

        // When
        mailService.sendOtpMail(email, otp);

        // Then
        verify(mailSender).send(any(MimeMessagePreparator.class));
    }

    @Test
    @DisplayName("sendOtpMail 실패 - 잘못된 파라미터")
    void sendOtpMail_fail_InvalidParameter() throws IOException {
        // Given
        final Email email = new Email("chickenman10@naver.com");
        final OtpNo otp = new OtpNo("123456");

        // When - Then
        assertThrows(InvalidParameterException.class, () -> mailService.sendOtpMail(email, null));
        assertThrows(InvalidParameterException.class, () -> mailService.sendOtpMail(null, otp));
        assertThrows(InvalidParameterException.class, () -> mailService.sendOtpMail(null, null));
    }

}