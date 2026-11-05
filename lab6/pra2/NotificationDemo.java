// Functional interface for notifier
@FunctionalInterface
interface Notifier {
    void send(String message);
}
// Marker interface for urgent notifiers
interface Urgent {}
// UrgentEmail implementation
class UrgentEmail implements Notifier, Urgent {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
public class NotificationDemo {
    public static void main(String[] args) {
        // Lambdas for email and sms
        Notifier email = message -> System.out.println("Email: " + message);
        Notifier sms = message -> System.out.println("SMS: " + message);
        Notifier[] senders = { email, sms };
        String message = "Your lab session starts at 2 PM.";
        // Broadcast to all
        System.out.println("=== Broadcast ===");
        for (Notifier sender : senders) {
            sender.send(message);
        }
        // Send twice if marked urgent
        System.out.println("\n=== Urgent Notification ===");
        Notifier urgentEmail = new UrgentEmail();
        Notifier[] urgentSenders = { urgentEmail, sms };
        for (Notifier sender : urgentSenders) {
            sender.send(message);
            if (sender instanceof Urgent) {
                sender.send(message);
            }
        }
    }
}
