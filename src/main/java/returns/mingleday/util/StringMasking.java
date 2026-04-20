package returns.mingleday.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;

@Component
@RequiredArgsConstructor
public class StringMasking {
    public static String emailMasking(String email) {
        StringTokenizer st = new StringTokenizer(email, "@");
        String name = st.nextToken();
        String domain = st.nextToken();

        return name.substring(0, 3) + "*******@" + domain;
    }
}
