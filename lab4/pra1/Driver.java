/**
 * Student Name : Krutin
 * Enrolment No : 25CS034
 * Course       : CEUE203 - Object Oriented Programming
 */
public class Driver {
    public static void main(String[] args) {
        System.out.println("Enrolment No: 25CS034 | Name: Krutin");

        String[] passwords = {
            "abc",
            "Password",
            "Pass1234",
            "Abcd1234!"
        };

        for (String pw : passwords) {
            System.out.println("Password: " + pw);
            System.out.println("Length >= 8: " + PasswordChecker.hasLength(pw));
            System.out.println("Uppercase: " + PasswordChecker.hasUpperCase(pw));
            System.out.println("Digit: " + PasswordChecker.hasDigit(pw));
            System.out.println("Special: " + PasswordChecker.hasSpecialCharacter(pw));
            System.out.println("Strength: " + PasswordChecker.strength(pw));
            System.out.println("-------------------------------");
        }
    }
}
