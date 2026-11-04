/**
 * Student Name : Krutin
 * Enrolment No : 25CS034
 * Course       : CEUE203 - Object Oriented Programming
 */
public class Driver {
    public static void main(String[] args) {
        System.out.println("Enrolment No: 25CS034 | Name: Krutin");

        Point[] points = {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2),
            new Point(5, 6),
            new Point(3, 4)
        };

        int distinct = 0;
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

