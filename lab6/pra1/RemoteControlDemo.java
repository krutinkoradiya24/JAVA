// Switchable interface with default toggle method
interface Switchable {
    void on();
    void off();
    boolean isOn();
    default void toggle() {
        if (isOn()) {
            off();
        } else {
            on();
        }
    }
}
// Fan class
class Fan implements Switchable {
    private boolean isOn = false;
    @Override
    public void on() {
        isOn = true;
        System.out.println("Fan is ON");
    }
    @Override
    public void off() {
        isOn = false;
        System.out.println("Fan is OFF");
    }
    @Override
    public boolean isOn() {
        return isOn;
    }
}
// Light class
class Light implements Switchable {
    private boolean isOn = false;
    @Override
    public void on() {
        isOn = true;
        System.out.println("Light is ON");
    }
    @Override
    public void off() {
        isOn = false;
        System.out.println("Light is OFF");
    }
    @Override
    public boolean isOn() {
        return isOn;
    }
}
// Functional interface
@FunctionalInterface
interface SwitchPolicy {
    boolean maySwitchOn(Switchable device, int hour);
}
public class RemoteControlDemo {
    public static void main(String[] args) {
        Switchable[] devices = {
            new Fan(),
            new Light()
        };
        // Toggle each device
        System.out.println("=== Toggling Devices ===");
        for (Switchable device : devices) {
            device.toggle();
        }
        // Policy using anonymous class
        SwitchPolicy dayPolicy = new SwitchPolicy() {
            @Override
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 18;
            }
        };
        // Policy using lambda expression
        SwitchPolicy nightPolicy =
            (device, hour) -> hour >= 18 || hour < 6;
        System.out.println("\n=== Policy Test ===");
        int hour = 10;
        for (Switchable device : devices) {
            System.out.println("Anonymous class policy: "
                    + dayPolicy.maySwitchOn(device, hour));
            System.out.println("Lambda policy: "
                    + nightPolicy.maySwitchOn(device, hour));
        }
    }
}
