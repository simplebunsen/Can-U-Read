package readability;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String fileInput = args[0];
        String text = new String(Files.readAllBytes(Paths.get(fileInput)));

        System.out.println("The text is:\n" + text);
        System.out.println();


//        Scanner scanner = new Scanner(System.in);
//        String text = scanner.nextLine();

        String[] sentences = text.split("[.?!]");

        int sentenceCounter = 0;
        int wordSum = 0;
        for (String sentence : sentences) {
            sentenceCounter++;
//            sentence = sentence.replaceAll("[^A-Za-z0-9\\s]", "");
//            System.out.println(sentence);
            String[] words = sentence.split(" ");
            List<String> temp = new ArrayList<>(Arrays.asList(words));
            temp.removeAll(Arrays.asList(""));
            wordSum += temp.size();
        }


        String textOnlyChars = text.replaceAll("\\s", "");

        double score = 4.71 * ((double) textOnlyChars.length() / wordSum) + 0.5 * ((double) wordSum / sentenceCounter) - 21.43;


        System.out.printf("Words: %d\n" +
                "Sentences: %d\n" +
                "Characters: %d\n" +
                "The score is: %.2f\n", wordSum, sentenceCounter, textOnlyChars.length(), score);

        int scoreInt = (int) Math.ceil(score);
        //System.out.println("ROUNDED SCORE: " + scoreInt);

        final String[] steps = {"5-6", "6-7", "7-9", "9-10", "10-11", "11-12", "12-13", "13-14", "14-15", "15-16",
                "16-17", "17-18", "18-24", "24+"};

        System.out.printf("This text should be understood by %s year olds.", steps[scoreInt-1]);
    }
}
