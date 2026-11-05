import java.util.Scanner;
// Enum for coin types
enum Coin {
    ONE, TWO, FIVE, TEN
}
public class VendingMachine {
    public static void main(String[] args) {
        // Snack price and initial total
        int price = 15;
        int total = 0;
        Scanner sc = new Scanner(System.in);
        // Keep taking coins until total reaches 15
        while (total < price) {
            System.out.print("Enter coin (ONE, TWO, FIVE, TEN): ");
            Coin coin = Coin.valueOf(sc.next().toUpperCase());
            // Get value from coin using switch expression
            int value = switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };
            total += value;
            System.out.println("Total so far: " + total);
        }
        // Return change
        int change = total - price;
        System.out.println("Paid. Change: " + change);
        sc.close();
    }
}
