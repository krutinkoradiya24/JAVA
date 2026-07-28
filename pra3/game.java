public class game {
    public static void main(String[] args) {

        card[] cards = {
            new card("Ace", "Spades"),
            new card("King", "Hearts"),
            new card("Queen", "Diamonds"),
            new card("Ace", "Spades"),
            new card("Jack", "Clubs")
        };

        boolean found = false;

        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < i; j++) {
                if (cards[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + cards[i]);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("No duplicate cards found.");
        }
    }
}