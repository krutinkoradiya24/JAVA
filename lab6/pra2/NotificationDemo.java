@FunctionalInterface
interface Notifier {
    void send(String message);
}

interface Urgent {}

class UrgentEmail implements Notifier, Urgent {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class NotificationDemo {
    public static void main(String[] args) {
        Notifier email = message -> System.out.println("Email: " + message);
        Notifier sms = message -> System.out.println("SMS: " + message);

        Notifier[] senders = { email, sms };
        String message = "Your lab session starts at 2 PM.";

        System.out.println("=== Broadcast ===");
        for (Notifier sender : senders) {
            sender.send(message);
        }

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
