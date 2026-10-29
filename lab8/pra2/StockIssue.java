import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OutOfStockException extends Exception {
    private int shortfall;

    public OutOfStockException(String message, int shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {
    private Map<String, Integer> stock = new HashMap<>();

    public Warehouse() {
        stock.put("Laptop", 5);
        stock.put("Mouse", 10);
        stock.put("Keyboard", 3);
    }

    public void issue(String item, int qty) throws OutOfStockException, InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than 0.");
        }

        if (!stock.containsKey(item)) {
            throw new OutOfStockException("Item '" + item + "' is not available.", qty);
        }

        int available = stock.get(item);
        if (qty > available) {
            int shortfall = qty - available;
            throw new OutOfStockException("Not enough stock for " + item + ". Available: " + available + ", Requested: " + qty, shortfall);
        }

        stock.put(item, available - qty);
        System.out.println("Issued " + qty + " " + item + "(s) successfully.");
    }
}

class Request {
    String item;
    int quantity;

    Request(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}

public class StockIssue {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        List<Request> requests = Arrays.asList(
            new Request("Laptop", 2),
            new Request("Mouse", 20),
            new Request("Keyboard", 0),
            new Request("Keyboard", 2),
            new Request("Monitor", 1),
            new Request("Laptop", 1)
        );

        System.out.println("=== Stock Issue Processing ===");
        for (Request request : requests) {
            System.out.println("\nRequest: " + request.item + " x " + request.quantity);
            try {
                warehouse.issue(request.item, request.quantity);
            } catch (OutOfStockException e) {
                System.out.println("OUT OF STOCK: " + e.getMessage());
                System.out.println("Shortfall: " + e.getShortfall());
            } catch (InvalidQuantityException e) {
                System.out.println("INVALID QUANTITY: " + e.getMessage());
            }
        }

        System.out.println("\nAll requests processed.");
    }
}
