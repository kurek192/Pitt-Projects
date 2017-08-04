//WILLLIAM KUREK
//PROJECT 1
//CS 1501
import java.util.*;
import java.io.*;

public class Anagram {

    static int numWords;
    static DictInterface dictionary;
    static ArrayList<SortedSet<String>> anagrams;
    static Scanner dScan, inScan;
    static PrintWriter writer;
    static String dictionaryObject;

    public static void main(String[] args) {
        // Generate dictionary and get input strings
        try {
            dictionaryObject = args[2];
            dScan = new Scanner(new File("dictionary.txt"));
            inScan = new Scanner(new File(args[0]));
            writer = new PrintWriter(new File(args[1]));
        } catch (Exception e) {
            System.out.println("Invalid arguments!");
            System.out.println(e);
            System.exit(0);
        }

        switch (dictionaryObject) {
            case "orig":
                dictionary = new MyDictionary();
                break;
            case "dlb":
                dictionary = new DLB();
                break;
            default:
                System.out.println("Invalid dictionary object!");
                System.exit(0);
        }

        generate();

    }

    public static void generate() {

        double totalTime = 0;
        double startTime = 0, endTime;
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        while (dScan.hasNext()) {
            dictionary.add(dScan.nextLine());
        }
        ArrayList<String> fileIn = new ArrayList<String>();
        while (inScan.hasNext()) {
            fileIn.add(inScan.nextLine().replaceAll("\\s", ""));
        }

        for (String str : fileIn) { //generate anagram file

            startTime = start.getTimeInMillis();
            numWords = 0;
            anagrams = new ArrayList<SortedSet<String>>();
            anagrams.add(new TreeSet<String>());
            writer.printf("Here are the results for %s:\n", str);
            StringBuilder sb = new StringBuilder("");
            char[] characterArray = str.toCharArray();
            ArrayList<Character> characters = new ArrayList<Character>();
            for (int i = 0; i < characterArray.length; i++) {
                characters.add(characterArray[i]);
            }
            findAnagrams(sb, characters);
            for (int i = 0; i < anagrams.size(); i++) {
                SortedSet<String> list = anagrams.get(i);
                if (!list.isEmpty()) {
                    writer.printf("%d word solutions:\n", i + 1);
                    for (String anagram : list) {
                        writer.println(anagram);
                    }
                }
            }
            writer.println();

        }

        Calendar stop = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        endTime = stop.getTimeInMillis();
        totalTime = endTime - startTime;

        System.out.println("Runtime: " + (int) totalTime + " Milliseconds");

        writer.close();
    }

    private static void findAnagrams(StringBuilder sb, ArrayList<Character> characters) {

        numWords = 1;
        for (int i = 0; i < characters.size(); i++) {
            // New sb stringbuilder for each starting character
            sb = new StringBuilder();
            char character = characters.get(0);
            sb.append(character);
            characters.remove(0);
            findAnagramsHelper(sb, characters);
            characters.add(character);
        }
    }

    private static void findAnagramsHelper(StringBuilder sb, ArrayList<Character> characters) {
        int start = sb.lastIndexOf(" ") + 1;
        int end = sb.length() - 1;
        int size = characters.size();
        int isWord = dictionary.searchPrefix(sb, start, end);
        if (size == 0) {
            // if sb and all characters used, add to list
            if (isWord == 2 || isWord == 3) {
                anagrams.get(numWords - 1).add(sb.toString());
            }
            return;
        }
        switch (isWord) {
            case 0:
                // Anagram is neither prefix nor word
                return;
            case 1:
                // Anagram is prefix not word, and characters not used up
                for (int i = 0; i < size; i++) {
                    char character = characters.get(0);
                    sb.append(character);
                    characters.remove(0);
                    findAnagramsHelper(sb, characters);
                    characters.add(character);
                    sb.deleteCharAt(sb.length() - 1);
                }
                return;
            case 2:
                // Anagram is word, not prefix, and characters not used up
                // Check for multi word anagrams
                sb.append(" ");
                numWords++;
                if (anagrams.size() <= numWords) {
                    anagrams.add(new TreeSet<String>());
                }
                for (int i = 0; i < size; i++) {
                    char character = characters.get(0);
                    sb.append(character);
                    characters.remove(0);
                    findAnagramsHelper(sb, characters);
                    characters.add(character);
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.deleteCharAt(sb.length() - 1);
                numWords--;
                return;
            case 3:
                // Anagram is both word and prefix, and characters not used up
                // Check for multi word anagrams and continue checking for singles
                sb.append(" ");
                numWords++;
                if (anagrams.size() <= numWords) {
                    anagrams.add(new TreeSet<String>());
                }
                for (int i = 0; i < size; i++) {
                    char character = characters.get(0);
                    sb.append(character);
                    characters.remove(0);
                    findAnagramsHelper(sb, characters);
                    characters.add(character);
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.deleteCharAt(sb.length() - 1);
                numWords--;
                for (int i = 0; i < size; i++) {
                    char character = characters.get(0);
                    sb.append(character);
                    characters.remove(0);
                    findAnagramsHelper(sb, characters);
                    characters.add(character);
                    sb.deleteCharAt(sb.length() - 1);
                }
                return;
        }

    }
}
