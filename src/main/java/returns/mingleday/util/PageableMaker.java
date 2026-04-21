package returns.mingleday.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import returns.mingleday.response.code.PaginationExceptionCode;
import returns.mingleday.response.exception.BaseException;

public class PageableMaker {
    private static final Integer PAGE = 0;
    private static final Integer SIZE = 20;
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    public static Pageable of(String sort) {
        return of(PAGE, SIZE, sort);
    }

    public static Pageable of(String sort, String direction) {
        validDirection(direction);
        return of(PAGE, SIZE, sort, direction);
    }

    public static Pageable of(Integer page, Integer size) {
        return PageRequest.of(page, size);
    }

    public static Pageable of(Integer page, Integer size, String sort) {
        isNegative(page);
        isNegative(size);
        return of(page, size, sort, DESC);
    }

    public static Pageable of(Integer page, Integer size, String sort, String direction) {
        isNegative(page);
        isNegative(size);
        validDirection(direction);
        Sort s = Sort.by(Sort.Direction.fromString(direction), sort);
        return PageRequest.of(page, size, s);
    }

    private static void isNegative(Integer number) {
        if(number < 0) {
            throw new BaseException(PaginationExceptionCode.CANNOT_USE_NEGATIVE_PARAMETER);
        }
    }

    private static void validDirection(String direction) {
        if(!direction.equalsIgnoreCase(ASC) && !direction.equalsIgnoreCase(DESC)) {
            throw new BaseException(PaginationExceptionCode.CANNOT_USE_DIRECTION_VALUE);
        }
    }
}