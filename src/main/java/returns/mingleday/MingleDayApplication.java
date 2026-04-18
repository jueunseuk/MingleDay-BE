package returns.mingleday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaAuditing
public class MingleDayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MingleDayApplication.class, args);
        System.out.println("서버 시작 : "+ LocalDateTime.now());
    }
}