//Coordinate
//stores coordinate cost and other information for the A star algorithm
public class Coordinate {

        int heuristic = 0; //Heuristic cost
        int cost = 0; //G+H
        int i, j;
        Coordinate parent;

        Coordinate(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "[" + this.i + ", " + this.j + "]";
        }
    } 