package returns.mingleday.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class StringParser {
    public static Integer getCharacterType(char c) {
        if(Character.isLetter(c)) {
            return 1;
        } else if(Character.isDigit(c)) {
            return 2;
        } else if(ALLOWED_CHARS.contains(c)) {
            return 3;
        }

        return -1;
    }

    private static final Set<Character> ALLOWED_CHARS = Set.of(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '[', ']', '{', '}', '.', '?'
    );
}
