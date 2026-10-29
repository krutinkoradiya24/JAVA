import java.util.InputMismatchException;
import java.util.Scanner;

class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            try {
                System.out.print("Enter first number: ");
                double num1 = sc.nextDouble();

                System.out.print("Enter operator (+, -, *, /): ");
                char operator = sc.next().charAt(0);

                System.out.print("Enter second number: ");
                double num2 = sc.nextDouble();

                double result;
                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 == 0) {
                            throw new DivideByZeroException("Cannot divide by zero.");
                        }
                        result = num1 / num2;
                        break;
                    default:
                        System.out.println("Invalid operator.");
                        continue;
                }

                System.out.println("Result = " + result);
                success = true;

            } catch (InputMismatchException e) {
                System.out.println("Invalid number input. Please enter numbers only.");
                sc.nextLine();
            } catch (DivideByZeroException e) {
                System.out.println("Calculation error: " + e.getMessage());
                sc.nextLine();
            } finally {
                System.out.println("Attempt logged.");
            }
        }

        System.out.println("Valid calculation completed.");
        sc.close();
    }
}
