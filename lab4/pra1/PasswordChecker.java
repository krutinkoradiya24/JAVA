public class PasswordChecker {

    public static boolean hasLength(String pw) {
        return pw != null && pw.length() >= 8;
    }

    public static boolean hasUpperCase(String pw) {
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isUpperCase(pw.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDigit(String pw) {   
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isDigit(pw.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSpecialCharacter(String pw) {
        for (int i = 0; i < pw.length(); i++) {
            char ch = pw.charAt(i);

            if (!Character.isLetterOrDigit(ch)) {
                return true;
            }
        }
        return false;
    }

    public static String strength(String pw) {
        int count = 0;

        if (hasLength(pw))
            count++;
        if (hasUpperCase(pw))
            count++;
        if (hasDigit(pw))
            count++;
        if (hasSpecialCharacter(pw))
            count++;

        if (count <= 1)
            return "Weak";
        else if (count <= 3)
            return "Medium";
        else
            return "Strong";
    }
}