//WILLIAM KUREK
//PROJECT 1
//CS 1501
public class DLB implements DictInterface {

    private Node head;

    public DLB() {
        head = null;
    }

    private class Node {

        private char data;
        private Node sibling;
        private Node child;

        private Node(char c) {
            data = c;
            sibling = null;
            child = null;
        }
    }

    public boolean add(String s) {
        /* Add a new String to the end of the DictInterface
         */
        if (s == null || s.length() == 0) {
            return false;
        }
        if (head == null) {
            head = new Node(s.charAt(0));
        }
        Node current = head;
        int index;

        for (index = 0; current.child != null; index++) {
            boolean found = false;
            if (current.data == s.charAt(index)) {
                found = true;
            }
            while (current.sibling != null && !found) {
                current = current.sibling;
                if (current.data == s.charAt(index)) {
                    found = true;
                }
            }
            if (!found) {
                current.sibling = new Node(s.charAt(index));
                current = current.sibling;
            }
            if (current.child != null) {
                current = current.child;
            }
        }

        for (; (index < s.length()) && (s.length() != 1); index++) {
            current.child = new Node(s.charAt(index));
            current = current.child;
        }
        current.child = new Node('$');
        return true;
    }

    /*
		return 0 for neither
		return 1 for isPrefix
		return 2 for isWord
		return 3 for both
     */
    public int searchPrefix(StringBuilder s) {
        // No string to check or no words in dictionary
        if (s == null || s.length() == 0 || head == null) {
            return 0;
        }
        return searchPrefixHelper(head, s, 0);
    }

    public int searchPrefix(StringBuilder s, int start, int end) {

        if (s == null || s.length() == 0 || head == null) {
            return 0;
        }

        if (start < 0 || end > s.length()) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(s.substring(start, end + 1));
        return searchPrefixHelper(head, sb, 0);
    }

    private int searchPrefixHelper(Node current, StringBuilder sb, int index) {
        boolean isPrefix = false;
        boolean isWord = false;
        if (current == null) {
            return 0;
        }
        if (index == sb.length()) {
            if (current.child != null) {
                isPrefix = true;
            }
            if (current.data == '$') {
                if (current.sibling != null) {
                    isPrefix = true;
                    isWord = true;
                } else {
                    isWord = true;
                }
            }
        }
        if (isPrefix && isWord) {
            return 3;
        } else if (isPrefix) {
            return 1;
        } else if (isWord) {
            return 2;
        }

        boolean found = false;
        if (current.data == sb.charAt(index)) {
            found = true;
        }
        while (current.sibling != null && !found) {
            current = current.sibling;
            if (current.data == sb.charAt(index)) {
                found = true;
            }
        }
        if (!found) {
            return 0;
        }
        return searchPrefixHelper(current.child, sb, index + 1);
    }

}
