package util;
import java.util.regex.Pattern;
public class Validator {
    private static final Pattern MOBILE = Pattern.compile("[6-9][0-9]{9}");
    private static final Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    public static boolean isValidMobile(String value) {
        return value != null && MOBILE.matcher(value).matches();
    }
    public static boolean isValidEmail(String value) {
        return value != null && EMAIL.matcher(value).matches();
    }
    public static boolean isValidAmount(long amount) {
        return amount > 0;
    }
}
