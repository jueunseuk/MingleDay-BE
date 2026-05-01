package returns.mingleday.util;

import returns.mingleday.response.code.CategoryExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.util.regex.Pattern;

public class ColorUtil {
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^[0-9A-Fa-f]{6}$");

    public static void validateHex(String color) {
        if (!HEX_COLOR_PATTERN.matcher(color).matches()) {
            throw new BaseException(CategoryExceptionCode.INVALID_COLOR_FORMAT);
        }
    }

    public static double getContrastRatio(String hex1, String hex2) {
        double l1 = getLuminance(hex1);
        double l2 = getLuminance(hex2);

        double lighter = Math.max(l1, l2);
        double darker = Math.min(l1, l2);

        return (lighter + 0.05) / (darker + 0.05);
    }

    private static double getLuminance(String hex) {
        int r = Integer.valueOf(hex.substring(0, 2), 16);
        int g = Integer.valueOf(hex.substring(2, 4), 16);
        int b = Integer.valueOf(hex.substring(4, 6), 16);

        double rs = transform(r / 255.0);
        double gs = transform(g / 255.0);
        double bs = transform(b / 255.0);

        return 0.2126 * rs + 0.7152 * gs + 0.0722 * bs;
    }

    private static double transform(double c) {
        return (c <= 0.03928) ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
    }
}