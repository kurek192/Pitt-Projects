//WILLIAM KUREK
//CS 1501 SUMMER 2017
//ASSIGNMENT 2

import java.util.Iterator;
import java.util.ArrayList;

public class PHPArray<V> implements Iterable<V> {

    private int N;	//number of key-value pairs in the symbol table
    private int M;	// size of linear probing table

    //Represents the hash table that contains nodes, and the node pointers
    private Node<V>[] nodes;
    private Node<V> first;
    private Node<V> last;
    private Node<V> current;

    /**
     * Initializes the PHPArray
     *
     * @param INIT_CAPACITY, intended start size
     */
    public PHPArray(int INIT_CAPACITY) {
        N = 0;
        M = INIT_CAPACITY;
        @SuppressWarnings("unchecked")
        Node<V>[] table = (Node<V>[]) new Node<?>[M];
        nodes = table;
        first = null;
        last = null;
        current = null;
    }

    /**
     * Returns the size of the array (key-value pairs)
     *
     * @return N, the number of key-value pairs inside the table
     */
    public int length() {
        return N;
    }

    /**
     * Returns true or false based on whether empty or not
     *
     * @return whether size is 0 or not.
     */
    public boolean isEmpty() {
        return length() == 0;
    }

    /**
     * insert the key-value pair into the symbol table
     *
     * @param k = key
     * @param v = value
     */
    public void put(String k, V v) {
        if (k == null) {
            return;
        }
        if (v == null) {
            unset(k);
        }
        if (N >= M / 2) {
            resize(2 * M);
        }

        int i;
        for (i = hash(k, M); nodes[i] != null; i++) {
            if (nodes[i].data.key.equals(k)) {
                nodes[i].data.value = v;
                return;
            }
        }

        Pair<V> tempPair = new Pair<V>(k, v);
        Node<V> temp = new Node<V>(tempPair);

        nodes[i] = temp;
        if (first == null) {
            first = nodes[i];
            current = first;
        }
        if (last == null) {
            last = nodes[i];
        }

        nodes[i].previous = last;
        last.next = nodes[i];
        last = nodes[i];

        N++;
    }

    /**
     * If the user decides to use int value for the string convieniently
     * converts that into a string then calls the original put()
     *
     * @param i, this is the key to be converted to String
     * @param v, this is the value
     */
    public void put(Integer i, V v) {
        String k = i.toString();
        if (k == null) {
            return;
        }
        if (v == null) {
            unset(k);
        }
        put(k, v);
    }

    /**
     * Resizes the hash table if necessary
     *
     * @param the new intended capacity
     */
    private void resize(int capacity) {
        System.out.print("\t\tSize: " + N);
        System.out.println(" -- resizing array from " + M + " to " + capacity);

        @SuppressWarnings("unchecked")
        Node<V>[] temp = (Node<V>[]) new Node<?>[capacity];
        for (int i = 0; i < M; i++) {
            if (nodes[i] != null) {
                temp[hash(nodes[i].data.key, capacity)] = nodes[i];
            }
        }
        nodes = temp;
        M = capacity;
    }

    /**
     * Get the value for any given key
     *
     * @param k, the key to be searched for
     * @return the value at that key k, or null if empty
     */
    public V get(String k) {
        for (int i = hash(k, M); nodes[i] != null; i = (i + 1) % M) {
            if (nodes[i].data.key.equals(k)) {
                return nodes[i].data.value;
            }
        }
        return null;
    }

    /**
     * If the value you'd like to get is type integer, convert to String and
     * call regular get
     *
     * @param i, the key you'd like to search for.
     * @return value associated from original get() method
     */
    public V get(Integer i) {
        String k = i.toString();
        return get(k);
    }

    /**
     * Deletes the key and associated value from the table and rehashes if
     * necessary
     *
     * @param k, the key
     */
    public void unset(String k) {
        if (!contains(k)) {
            return;
        }

        //Finds the position of the key i
        int i = hash(k, M);
        while (!k.equals(nodes[i].data.key)) {
            i = (i + 1) % M;
        }

        //Deletes the key and it's associated value
        if (last == nodes[i]) {
            last = nodes[i].previous;
        }
        if (first == nodes[i]) {
            first = nodes[i].next;
        }

        nodes[i].previous.next = nodes[i].next;
        nodes[i].next.previous = nodes[i].previous;
        nodes[i] = null;

        //Rehashes all the keys in the same cluster
        i = (i + 1) % M;
        while (nodes[i] != null) {
            Node<V> temp = nodes[i];
            String keyToRehash = nodes[i].data.key;
            System.out.println("\t\tKey " + keyToRehash + " rehashed...\n");
            nodes[i] = null;
            int hash = hash(keyToRehash, M);
            while (nodes[hash] != null) {
                hash++;
            }
            nodes[hash] = temp;

        }
        N--;
    }

    /**
     * Converts integer key values to String and then calls original unset()
     * method to delete
     *
     * @param i, the key
     */
    public void unset(Integer i) {
        String k = i.toString();
        unset(k);
    }

    /**
     * Determines whether or not the table contains a (key, value) pair with the
     * associated key
     *
     * @param k, the key
     * @return true or false whether key is contained or not.
     */
    public boolean contains(String k) {
        return get(k) != null;
    }

    /**
     *
     *
     */
    public Pair<V> each() {
        if (current == null) {
            reset();
            return null;
        }

        Pair<V> currentPair = current.data;
        current = current.next;
        return currentPair;
    }

    /**
     * Assigns new keys to the values starting at 0 and ending a length()-1
     */
    public void sort() {

        if (!(first.data.value instanceof Comparable)) {
            throw new ClassCastException();
        }
        if (first == null || first.next == null) {
            return;
        }

        //Creates array without null nodes in table
        Node<V>[] a = toArray();
        @SuppressWarnings("unchecked")
        Node<V>[] temp = (Node<V>[]) new Node<?>[N];

        mergeSort(a, temp, 0, N - 1);

        //Creates new linked list and generates the keys
        first = a[0];
        nodes[0] = a[0];
        nodes[0].data.key = "0";

        int i;
        for (i = 1; i < a.length; i++) {
            String key = Integer.toString(i);
            nodes[hash(key, M)] = a[i];
            nodes[hash(key, M)].data.key = key;
            a[i - 1].next = a[i];
            a[i].previous = a[i - 1];
        }

        a[i - 1].next = null;
    }

    /**
     * Sorts the values just like sort, but instead of reassigning the keys to
     * ints starting at 0, we keep the keys as they were firstly.
     */
    public void asort() {
        if (!(first.data.value instanceof Comparable)) {
            throw new IllegalArgumentException();
        }
        if (first == null || first.next == null) {
            return;
        }

        // need array without the null elements of nodes
        Node<V>[] a = toArray();

        @SuppressWarnings("unchecked")
        Node<V>[] temp = (Node<V>[]) new Node<?>[N];
        mergeSort(a, temp, 0, N - 1);
        // create new linked list
        first = a[0];
        int i;
        for (i = 1; i < N; i++) {
            a[i - 1].next = a[i];
            a[i].previous = a[i - 1];
        }
        last = a[N - 1];

        for (i = 0; i < N; i++) {

        }

        a[i - 1].next = null;

    }

    /**
     * Simple mergeSort algorithm
     */
    private void mergeSort(Node<V>[] a, Node<V>[] temp, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, temp, left, center);
            mergeSort(a, temp, center + 1, right);
            merge(a, temp, left, center + 1, right);
        }
    }

    /**
     * Part of mergeSort algorithm
     */
    private void merge(Node<V>[] a, Node<V>[] temp, int left, int middle, int right) {
        int leftEnd = middle - 1;
        int k = left;
        int num = right - left + 1;

        while (left <= leftEnd && middle <= right) {
            if (a[left].compareTo(a[middle]) <= 0) {
                temp[k++] = a[left++];
            } else {
                temp[k++] = a[middle++];
            }
        }
        // Copy rest of first half
        while (left <= leftEnd) {
            temp[k] = a[left];
            k++;
            left++;
        }
        // Copy rest of second half
        while (middle <= right) {
            temp[k] = a[middle];
            k++;
            middle++;
        }
        // Copy temp back
        for (int i = 0; i < num; i++, right--) {
            a[right] = temp[right];
        }
    }

    /**
     * Converts node linked list into an array ignoring null elements
     */
    public Node<V>[] toArray() {

        @SuppressWarnings("unchecked")
        Node<V>[] temp = (Node<V>[]) new Node<?>[N];
        Node<V> curr = first;
        int index = 0;
        while (index < N) {
            temp[index] = curr;
            curr = curr.next;
            index++;
        }
        reset();
        return temp;
    }

    public ArrayList<String> keys() {

        Node<V> curr = first;
        ArrayList<String> list = new ArrayList<String>();
        int index = 0;
        while (index < N) {
            list.add(curr.data.key);
            curr = curr.next;
            index++;
        }
        reset();
        return list;
    }

    public ArrayList<V> values() {

        Node<V> curr = first;
        ArrayList<V> list = new ArrayList<V>();
        int index = 0;
        while (index < N) {
            list.add(curr.data.value);
            curr = curr.next;
            index++;
        }
        return list;
    }

    /**
     * For grading purposes, determines if the table is set up correctly hashing
     * values properly by printing table
     */
    public void showTable() {
        System.out.println("\tRaw Hash Table Contents:");
        for (int i = 0; i < M; i++) {
            System.out.print(i + ": ");
            if (nodes[i] == null) {
                System.out.println("null");
            } else {
                nodes[i].printData();
            }
        }
    }

    public PHPArray<String> array_flip() {
        PHPArray<String> temp = new PHPArray<String>(M);

        Node<V> curr = current;
        while (curr != null) {

            if (curr.data.value instanceof String) {
                temp.put((String) curr.data.value, curr.data.key);
                curr = curr.next;
            } else {
                throw new ClassCastException("Cannot convert class java.lang.Integer to String");
            }
        }

        return temp;
    }

    /**
     * Hash function for keys - returns value between 0 and M-1
     *
     * @param key, the key
     * @param M, the capacity
     *
     */
    private int hash(String key, int M) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    /**
     * Sets each iteration value back to the beginning
     */
    public void reset() {
        current = first;
    }

    /**
     * Allows for easy iteration through the values within the PHPArray
     */
    public Iterator<V> iterator() {
        return new ListInterator();
    }

    /**
     * Inner class that allows user to extract (key, value) pairs whilst still
     * maintaining a good level of data encapsulation
     */
    public static class Pair<V> {

        public String key;
        public V value;

        private Pair(String k, V v) {
            key = k;
            value = v;
        }

    }

    /**
     * Node that holds the key, value pair information and the linked list
     * information.
     */
    private static class Node<V> implements Comparable<Node<V>> {

        private Pair<V> data;
        private Node<V> next;
        private Node<V> previous;

        private Node(Pair<V> d) {
            data = d;
        }

        // used for next() return
        @Override
        public String toString() {
            return data.value.toString();
        }

        // used for showTable()
        private void printData() {
            System.out.println("Key: " + data.key + " Value: " + data.value);
        }

        @Override
        public int compareTo(Node<V> n) {
            V thisData = this.data.value;
            V nData = n.data.value;

            @SuppressWarnings("unchecked")
            int compare = ((Comparable<V>) thisData).compareTo(nData);
            return compare;
        }
    }

    /**
     * Iterator for the linked list
     */
    private class ListInterator implements Iterator<V> {

        private Node<V> current;

        public ListInterator() {
            current = first;
        }

        public boolean hasNext() {
            if (current != null) {
                return true;
            }
            return false;
        }

        public V next() {
            V val = current.data.value;
            current = current.next;
            return val;
        }
    }
}
