package returns.mingleday.service.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public void isValidPassword(String password) {
        if(password == null || password.length() < 8 || password.length() > 20) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }

        char[] chars = password.toCharArray();
        int alphabet = 0;
        int number = 0;
        int symbol = 0;

        for(char c : chars) {
            switch(getCharacterType(c)) {
                case 1: alphabet++; break;
                case 2: number++; break;
                case 3: symbol++; break;
                default: throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
            }
        }

        if(alphabet < 1 || number < 1 || symbol < 1) {
            throw new BaseException(GlobalExceptionCode.INVALID_VALUE_REQUEST);
        }
    }

    private Integer getCharacterType(char c) {
        if(Character.isLetter(c)) {
            return 1;
        } else if(Character.isDigit(c)) {
            return 2;
        } else if(SPECIAL_CHARS.contains(c)) {
            return 3;
        }

        return -1;
    }

    private static final Set<Character> SPECIAL_CHARS = Set.of(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '[', ']', '{', '}', '.', '?'
    );
}
