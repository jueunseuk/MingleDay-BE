package returns.mingleday.service.users;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.users.Email;
import returns.mingleday.domain.users.Purpose;
import returns.mingleday.domain.users.User;
import returns.mingleday.repository.EmailRepository;
import returns.mingleday.repository.UserRepository;
import returns.mingleday.response.code.EmailExceptionCode;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.code.UserExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.util.Masking;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Transactional
    public void sendMail(String inputEmail, Purpose purpose) {
        String authCode = generateVerificationCode();

        Optional<User> user = userRepository.findByEmail(inputEmail);
        if(user.isPresent() && purpose == Purpose.REGISTER) {
            log.info("Attempt to signup an existing user - userId: {}", user.get().getUserId());
            throw new BaseException(UserExceptionCode.ALREADY_REGISTERED_EMAIL);
        }

        Email email = Email.of(inputEmail, purpose, authCode);
        emailRepository.save(email);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(inputEmail);
            helper.setSubject("MingleDay 서비스 인증 코드입니다.");

            StringBuilder sb = new StringBuilder();
            sb.append("인증 코드: ").append(authCode).append("\n");
            switch (purpose) {
                case REGISTER -> sb.append("회원가입을 위한 인증코드 6자리입니다.");
                case REISSUE -> sb.append("비밀번호 변경을 위한 인증코드 6자리입니다.");
                case WITHDRAWAL -> sb.append("회원탈퇴를 위한 인증코드 6자리입니다.");
            }
            sb.append("\n\n만약 본인이 발송한 인증 코드가 아니라면 현재 이메일로 문의 바랍니다.").append("\n");

            helper.setText(sb.toString(), true);
            mailSender.send(message);

            log.info("Email send - email: {}, purpose: {}", Masking.emailMasking(email.getEmail()), purpose);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(EmailExceptionCode.FAILED_TO_SEND_CODE);
        }
    }

    @Transactional
    public void verifyCode(String email, String code, Purpose purpose) {
        Email check = emailRepository.findFirstByEmailAndPurpose(email, purpose);

        if(check == null) {
            log.info("Non-existent mail verification - email: {}, purpose: {}", Masking.emailMasking(email), purpose);
            throw new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND);
        }

        if(check.getIsVerified()) {
            throw new BaseException(EmailExceptionCode.ALREADY_VERIFY_REQUEST);
        }

        if(!check.getCode().equals(code)) {
            log.info("Verify code mismatch - email: {}, correctCode: {}, inputCode: {}", Masking.emailMasking(email), check.getCode(), code);
            throw new BaseException(EmailExceptionCode.AUTHENTICATION_CODE_DOES_NOT_MATCH);
        }

        check.verify();
        log.info("Success to verify email - email: {}, purpose: {}", Masking.emailMasking(email), purpose);
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
