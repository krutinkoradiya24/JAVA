public class Driver {

    public static void main(String[] args) {

        String[] passwords = {
                "abc",
                "Password",
                "Pass1234",
                "Abcd1234!",
                "hello123",
                "HELLO@12"
        };

        for (String pw : passwords) {

            System.out.println("Password: " + pw);

            System.out.println("Length >= 8      : " + PasswordChecker.hasLength(pw));
            System.out.println("Uppercase Letter : " + PasswordChecker.hasUpperCase(pw));
            System.out.println("Digit            : " + PasswordChecker.hasDigit(pw));
            System.out.println("Special Character: " + PasswordChecker.hasSpecialCharacter(pw));

            System.out.println("Strength: " + PasswordChecker.strength(pw));
            System.out.println("-------------------------------");
        }
    }
}