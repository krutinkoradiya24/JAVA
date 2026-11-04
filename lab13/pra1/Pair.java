/**
 * Student Name : Krutin
 * Enrolment No : 25CS034
 * Course       : CEUE203 - Object Oriented Programming
 */
public class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
