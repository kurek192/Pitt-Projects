//WILLIAM KUREK
//PROJECT 4
//CS 445

/**
 * It is okay to use ArrayList class but you are not allowed to use any other
 * predefined class supplied by Java.
 */
import java.util.ArrayList;

public class CompressDecompress {

    /**
     * Get a string representing a Huffman tree where its root node is root
     *
     * @param root the root node of a Huffman tree
     * @return a string representing a Huffman tree
     */
    public static String getTreeString(final BinaryNodeInterface<Character> root) {
        String treeString = "";

        if (root == null) {
            return "";
        }

        if (root.isLeaf()) {
            treeString = "L" + root.getData().toString();
            return treeString;
        }

        treeString = "I";

        if (root.getLeftChild() != null) {
            treeString = treeString + getTreeString(root.getLeftChild());
        }
        if (root.getRightChild() != null) {
            treeString = treeString + getTreeString(root.getRightChild());
        }

        return treeString;

    }

    /**
     * Compress the message using Huffman tree represented by treeString
     *
     * @param root the root node of a Huffman tree
     * @param message the message to be compressed
     * @return a string representing compressed message.
     */
    public static String compress(final BinaryNodeInterface<Character> root, final String message) {
        String result = "";

        if (message.length() == 0) {
            return "";
        }

        for (int i = 0; i < message.length(); i++) {
            char temp = message.charAt(i);
            result = result + getPathTo(root, temp);

        }

        return result;

    }

    private static String getPathTo(final BinaryNodeInterface<Character> root, char c) {
        return getPathTo(root, c, "");
    }

    private static String getPathTo(final BinaryNodeInterface<Character> root, char c, String path) {

        if (root.isLeaf()) {
            if (root.getData() == c) {
                return path;
            } else {
                return null;
            }
        }

        if (root.getRightChild() != null) {
            String thisPath = getPathTo(root.getRightChild(), c, path + "1");
            if (thisPath != null) {
                return thisPath;
            }
        }

        if (root.getLeftChild() != null) {
            String thisPath = getPathTo(root.getLeftChild(), c, path + "0");
            if (thisPath != null) {
                return thisPath;
            }
        }
        return null;
    }

    /**
     * Decompress the message using Huffman tree represented by treeString
     *
     * @param treeString the string represents the Huffman tree of the
     * compressed message
     * @param message the compressed message to be decompressed
     * @return a string representing decompressed message
     */
    public static String decompress(final String treeString, final String message) {

        BinaryNodeInterface<Character> rootNode = new BinaryNode<Character>();
        generateTree(rootNode, treeString);
        String result = message;

        if (result.length() == 0) {
            return result;
        }

        while (result.charAt(0) == '0' || result.charAt(0) == '1') {
            result = traverse(rootNode, result);
            result = result.substring(1);
        }

        return result;
    }

    private static String traverse(final BinaryNodeInterface<Character> root, String result) {

        if (result.length() < 0) {
            return result;
        }

        if (root.isLeaf()) {
            result = "L" + result + "" + root.getData();
            return result;
        }

        if (result.charAt(0) == '0') {
            result = result.substring(1);
            result = traverse(root.getLeftChild(), result);
            if (result.charAt(0) == 'L') {
                return result;
            }
        }

        if (result.charAt(0) == '1') {
            result = result.substring(1);
            result = traverse(root.getRightChild(), result);
            if (result.charAt(0) == 'L') {
                return result;
            }
        }

        return result;
    }

    private static String generateTree(final BinaryNodeInterface<Character> root, String treeString) {

        if (treeString.length() == 0) {
            return treeString;
        } else if (treeString.charAt(0) == 'I') {
            root.setLeftChild(new BinaryNode<Character>());
            treeString = treeString.substring(1);
            treeString = generateTree(root.getLeftChild(), treeString);
            if (treeString.length() > 0) {
                root.setRightChild(new BinaryNode<Character>());
                treeString = generateTree(root.getRightChild(), treeString);
            }

            return treeString;
        } else {
            root.setData(treeString.charAt(1));
            treeString = treeString.substring(2);
            return treeString;
        }
    }

}
