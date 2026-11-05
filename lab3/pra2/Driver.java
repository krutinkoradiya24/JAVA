public class Driver {
    public static void main(String[] args) {
        // Cards list with a duplicate
        Card[] stream = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Diamonds"),
            new Card("Ace", "Spades"),
            new Card("Jack", "Clubs")
        };
        Card[] cards = new Card[stream.length];
        int count = 0;
        // Check each card against earlier cards
        for (Card c : stream) {
            boolean duplicate = false;
            for (int i = 0; i < count; i++) {
                if (c.equals(cards[i])) {
                    System.out.println("Duplicate found: " + c);
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                break;
            }
            cards[count++] = c;
        }
    }
}
