//PROBABILITYCOMPARE
//compares the probability of locations to determine the highest
import java.util.Comparator;

public class ProbabilityCompare implements Comparator<Map> {

        @Override
        public int compare(Map p1, Map p2) {
            return p1.getProbabilty().compareTo(p2.getProbabilty());
        }
    }