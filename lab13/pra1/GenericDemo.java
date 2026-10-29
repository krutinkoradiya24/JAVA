public class GenericDemo {
    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {
        Box<Integer> b1 = new Box<>(100);
        System.out.println("Box Integer: " + b1.get());
        b1.set(200);
        System.out.println("Updated Box: " + b1.get());

        Box<String> b2 = new Box<>("Hello");
        System.out.println("Box String: " + b2.get());

        Pair<String, Integer> p1 = new Pair<>("Age", 20);
        System.out.println("Pair First: " + p1.getFirst());
        System.out.println("Pair Second: " + p1.getSecond());

        Integer[] numbers = {10, 20, 30};
        System.out.println("First Integer: " + firstElement(numbers));

        String[] names = {"Java", "Python", "C++"};
        System.out.println("First String: " + firstElement(names));
    }
}
