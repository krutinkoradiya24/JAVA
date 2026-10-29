import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = "java is easy and java is powerful java is popular";
        String[] words = paragraph.toLowerCase().split("\\s+");

        HashMap<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(frequency.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println("Words sorted by frequency:");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
