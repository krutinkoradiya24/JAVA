public class Driver {
    public static void main(String[] args) {
        // Array with repeated points
        Point[] points = {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2),
            new Point(5, 6),
            new Point(3, 4)
        };
        int distinct = 0;
        // Check if each point was seen before
        for (int i = 0; i < points.length; i++) {
            boolean seenBefore = false;
            for (int j = 0; j < i; j++) {
                if (points[i].equals(points[j])) {
                    seenBefore = true;
                    break;
                }
            }
            if (!seenBefore) {
                distinct++;
            }
        }
        System.out.println("Distinct: " + distinct);
    }
}
