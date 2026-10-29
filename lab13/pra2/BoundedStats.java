public class BoundedStats {
    static <N extends Number> double average(N[] values) {
        double sum = 0;
        for (N n : values) {
            sum += n.doubleValue();
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        Integer[] intValues = {1, 2, 3, 4, 5};
        Double[] doubleValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        Long[] longValues = {10L, 20L, 30L, 40L, 50L};

        System.out.println("Integer Average: " + average(intValues));
        System.out.println("Double Average: " + average(doubleValues));
        System.out.println("Long Average: " + average(longValues));
    }
}
