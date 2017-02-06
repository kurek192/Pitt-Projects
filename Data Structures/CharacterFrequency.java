//William Kurek
//Project 1

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CharacterFrequency {

    public static void main(String[] args) throws IOException {

        File file = new File("letter1.txt");
        Scanner fileIn = new Scanner(file);
        fileIn.useDelimiter("\\s|\\,||\\.");

        FrequencyBag<String> fb = new FrequencyBag<String>();
        System.out.println("Character: Frequency");
        System.out.println("====================");

        while (fileIn.hasNext()) {

            String letter = fileIn.next();
            letter = letter.toLowerCase();;
            fb.add(letter);

        }

        for (char alpha = 'a'; alpha <= 'z'; alpha++) {
            String letter = String.valueOf(alpha);
            System.out.println(alpha + ": " + fb.getFrequencyOf(letter));
        }

    }

}
