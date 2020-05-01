package readability;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    final static String[] stringSteps = {"5-6", "6-7", "7-9", "9-10", "10-11", "11-12", "12-13", "13-14", "14-15", "15-16",
            "16-17", "17-18", "18-24", "24+",};

    final static int[] steps = {6, 7, 9, 10, 11, 12, 13, 14, 15, 16,
            17, 19, 24, 25, 99};



    public static void main(String[] args) throws IOException {
        String fileInput = args[0];
        String text = new String(Files.readAllBytes(Paths.get(fileInput)));

        System.out.println("The text is:\n" + text);
        System.out.println();


        Scanner scanner = new Scanner(System.in);

        String[] sentences = text.split("[.?!]");

        int sentenceCounter = 0;
        int wordSum = 0;
        int syllablesSum = 0;
        int polysyllablesSum = 0;
        for (String sentence : sentences) {
            sentenceCounter++;
//            sentence = sentence.replaceAll("[^A-Za-z0-9\\s]", "");
//            System.out.println(sentence);
            String[] words = sentence.split(" ");
            List<String> wordsInSentence = new ArrayList<>(Arrays.asList(words));
            wordsInSentence.removeAll(Arrays.asList(""));
            wordsInSentence.removeAll(Arrays.asList("\n"));
            syllablesSum += calculateSyllables(wordsInSentence)[0];
            polysyllablesSum += calculateSyllables(wordsInSentence)[1];
            wordSum += wordsInSentence.size();
        }


        String textOnlyChars = text.replaceAll("\\s", "");


        System.out.printf("Words: %d\n" +
                "Sentences: %d\n" +
                "Characters: %d\n" +
                "Syllables: %d\n" +
                "Polysyllables: %d\n", wordSum, sentenceCounter, textOnlyChars.length(), syllablesSum, polysyllablesSum);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        double automatedReadability = 4.71 * ((double) textOnlyChars.length() / wordSum) + 0.5 * ((double) wordSum / sentenceCounter) - 21.43;

        double fleschReadability = 0.39 * ((double) wordSum / sentenceCounter) + 11.8 * ((double)syllablesSum / wordSum) - 15.59;

        double measureGobbledygook = 1.043 * Math.sqrt(polysyllablesSum * (30d / sentenceCounter)) + 3.1291;

        double L = (double) textOnlyChars.length() / (double) wordSum * 100;
        double S = (double) sentenceCounter / (double) wordSum * 100;
        double colemanIndex = 0.0588 * L - 0.296 * S - 15.8;



        double score = 0;
        switch (scanner.nextLine()) {
            case "ARI":
                System.out.print("Automated Readability Index: ");
                break;
            case "FK":
                System.out.print("Flesch–Kincaid readability tests: ");
                break;
            case "SMOG":
                System.out.print("Simple Measure of Gobbledygook: ");
                break;
            case "CL":
                System.out.print("Coleman–Liau index: ");
                break;
            case "all":
                printAllIndices(automatedReadability, fleschReadability, measureGobbledygook, colemanIndex);
                double averageAge = (double) (steps[(int) Math.round(automatedReadability) -1] +
                                steps[(int) Math.round(fleschReadability) - 1] +
                                steps[(int) Math.round(measureGobbledygook) - 1] +
                                steps[(int) Math.round(colemanIndex)] - 1) / 4;
                System.out.printf("\nThis text should be understood in average by %s year olds.", averageAge);
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + scanner.nextLine());
        }
        System.out.printf("%.2f (about %s year olds).", score, steps[(int) (Math.round(score)-1)]);
    }

    private static void printAllIndices(double automatedReadability, double fleschReadability, double measureGobbledygook, double colemanIndex) {
//        System.out.println(automatedReadability);
//        System.out.println(fleschReadability);
//        System.out.println(measureGobbledygook);
//        System.out.println(colemanIndex);
        System.out.println();
        double score = automatedReadability;
        System.out.print("Automated Readability Index: ");
        System.out.printf("%.2f (about %s year olds).\n", score, steps[(int) (Math.round(score) - 1)]);
        score = fleschReadability;
        System.out.print("Flesch–Kincaid readability tests: ");
        System.out.printf("%.2f (about %s year olds).\n", score, steps[(int) (Math.round(score) - 1)]);
        score = measureGobbledygook;
        System.out.print("Simple Measure of Gobbledygook: ");
        System.out.printf("%.2f (about %s year olds).\n", score, steps[(int) (Math.round(score) - 1)]);
        score = colemanIndex;
        System.out.print("Coleman–Liau index: ");
        System.out.printf("%.2f (about %s year olds).\n", score, steps[(int) (Math.round(score) - 1)]);

    }

    private static int[] calculateSyllables(List<String> words) {
        int totalSyllables = 0;
        int totalPolysyllables = 0;
        //System.out.println(words);

        for (String word : words){
            //System.out.println("word is: '" + word + "'");
            int vowels = 0;
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                //System.out.println("position " + i);
                if (String.valueOf(chars[i]).matches("[aiouyAIOUY]")) {
                    //System.out.println("char matched aiouy, ++ vowel");
                    vowels++;
                    //next char is irrelevant since its not counted if its a vowel or a consonant, skip it.
                    i++;
                } else if (String.valueOf(chars[i]).matches("[eE]")) {
                    if (i != chars.length - 1){
                        //System.out.println("char matched e and isnt at the end, ++vowel");
                        vowels++;
                        i++;
                    }
                }
            }
            if (word.matches("\\s")) {
                System.out.println("empty space woulda added 1, skipping");
            } else if(vowels == 0) {
                vowels = 1;
            } else if (vowels >= 3){
                totalPolysyllables++;
            }
            totalSyllables += vowels;
        }
        return new int[]{totalSyllables, totalPolysyllables};
    }
}
