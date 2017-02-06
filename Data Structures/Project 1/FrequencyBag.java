//Wiliam Kurek
//Project 1

public class FrequencyBag<T> {

    private Node head;
    private int count;

    // TO DO: Instance Variables
    /**
     * Constructor Constructs an empty frequency bag.
     */
    public FrequencyBag() {

        head = new Node(null);
        count = 0;

    }

    /**
     * Adds new entry into this frequency bag.
     *
     * @param aData the data to be added into this frequency bag.
     */
    public void add(T aData) {
        if (count == 0) {
            Pair<T, Integer> firstPair = new Pair<T, Integer>(aData, 1);
            head = new Node(firstPair);
            count++;
            return;
        }
        Node current1 = head;
        Node frequencyNode = null;
        boolean exists = false;
        while (current1 != null) {
            Pair<T, Integer> headPair = (Pair<T, Integer>) current1.data;
            if (headPair.fst() == aData || headPair.fst().equals(aData)) {
                exists = true;
                frequencyNode = current1;
                break;
            } else {
                exists = false;
                current1 = current1.next;
            }
        }
        if (exists == true) {
            Pair<T, Integer> tempPair1 = (Pair<T, Integer>) frequencyNode.data;
            int amount = tempPair1.snd();
            amount++;
            tempPair1.setSnd(amount);
            frequencyNode.setData(tempPair1);
            count++;
        }
        if (exists == false) {
            Pair<T, Integer> pair = new Pair<T, Integer>(aData, 1);
            Node newNode = new Node(pair);
            Node current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
            count++;
        }
    }

    /**
     * Gets the number of occurrences of aData in this frequency bag.
     *
     * @param aData the data to be checked for its number of occurrences.
     * @return the number of occurrences of aData in this frequency bag.
     */
    public int getFrequencyOf(T aData) {
        if (head.isEmpty() == true) {
            return 0;
        }
        boolean exists = false;
        Node current1 = head;
        Node frequencyNode = null;
        while (current1 != null) {
            Pair<T, Integer> headPair = (Pair<T, Integer>) current1.data;
            if (headPair.fst() == aData || headPair.fst().equals(aData)) {
                frequencyNode = current1;
                exists = true;
                break;
            } else {
                current1 = current1.next;
                exists = false;
            }
        }
        if (exists == true) {
            Pair<T, Integer> tempPair1 = (Pair<T, Integer>) frequencyNode.data;
            int frequency = tempPair1.snd();
            return frequency;
        }
        return 0;
    }

    /**
     * Gets the maximum number of occurrences in this frequency bag.
     *
     * @return the maximum number of occurrences of an entry in this frequency
     * bag.
     */
    public int getMaxFrequency() {
        if (head.isEmpty() == true) {
            return 0;
        }
        Node current1 = head;
        Pair<T, Integer> maxPair = (Pair<T, Integer>) current1.data;
        int max = maxPair.snd();
        while (current1 != null) {
            Pair<T, Integer> headPair = (Pair<T, Integer>) current1.data;
            if (headPair.snd() > max) {
                max = headPair.snd();
            } else {
                current1 = current1.next;
            }
        }
        return max;
    }

    /**
     * Gets the probability of aData
     *
     * @param aData the specific data to get its probability.
     * @return the probability of aData
     */
    public double getProbabilityOf(T aData) {

        boolean exists = false;
        Node current1 = head;
        Node frequencyNode = null;
        while (current1 != null) {
            Pair<T, Integer> headPair = (Pair<T, Integer>) current1.data;
            if (headPair.fst() == aData || headPair.fst().equals(aData)) {
                frequencyNode = current1;
                exists = true;
                break;
            } else {
                current1 = current1.next;
                exists = false;
            }
        }
        if (exists == true) {
            Pair<T, Integer> probPair = (Pair<T, Integer>) frequencyNode.data;
            double frequency = probPair.snd();
            return frequency / count;
        }
        return 0;
    }

    /**
     * Empty this bag.
     */
    public void clear() {
        head = new Node(null);
        count = 0;
    }

    /**
     * Gets the number of entries in this bag.
     *
     * @return the number of entries in this bag.
     */
    public int size() {
        return count;
    }

    private class Node {

        private Node next;
        private Pair data;

        public Node(Pair data) {
            next = null;
            this.data = data;
        }

        public Node(Pair data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Pair getData() {
            return data;
        }

        public void setData(Pair data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public boolean isEmpty() {
            return data == null;
        }

    }

}
