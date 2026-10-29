class UnsafeSeatBooking {
    private int seatsLeft = 5;

    public boolean book(String name) {
        if (seatsLeft > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            seatsLeft--;
            System.out.println(name + " booked a seat.");
            return true;
        }
        System.out.println(name + " failed - no seats.");
        return false;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }
}

class SafeSeatBooking {
    private int seatsLeft = 5;

    public synchronized boolean book(String name) {
        if (seatsLeft > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            seatsLeft--;
            System.out.println(name + " booked a seat.");
            return true;
        }
        System.out.println(name + " failed - no seats.");
        return false;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }
}

public class SeatBookingRace {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Without Synchronization ===");
        UnsafeSeatBooking unsafeBooking = new UnsafeSeatBooking();
        Thread[] unsafeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            String name = "Customer-" + (i + 1);
            unsafeThreads[i] = new Thread(() -> unsafeBooking.book(name));
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) {
            t.join();
        }
        System.out.println("Seats remaining (Unsafe): " + unsafeBooking.getSeatsLeft());

        System.out.println("\n=== With Synchronization ===");
        SafeSeatBooking safeBooking = new SafeSeatBooking();
        Thread[] safeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            String name = "Customer-" + (i + 1);
            safeThreads[i] = new Thread(() -> safeBooking.book(name));
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) {
            t.join();
        }
        System.out.println("Seats remaining (Safe): " + safeBooking.getSeatsLeft());
    }
}
