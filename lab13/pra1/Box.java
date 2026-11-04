/**
 * Student Name : Krutin
 * Enrolment No : 25CS034
 * Course       : CEUE203 - Object Oriented Programming
 */
public class Box<T> {
    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
