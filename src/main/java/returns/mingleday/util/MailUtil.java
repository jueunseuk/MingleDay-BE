package returns.mingleday.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.user.Purpose;
import returns.mingleday.response.code.EmailExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailUtil {

    private final JavaMailSender mailSender;

    @Transactional
    public void sendMail(String email, String subject, String content, Purpose purpose) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

            log.info("Email send - email: {}, purpose: {}", StringMasking.emailMasking(email), purpose);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(EmailExceptionCode.FAILED_TO_SEND_CODE);
        }
    }
}
