import javax.swing.plaf.PanelUI;
import java.io.*;
import java.util.*;

import static java.awt.Color.RED;

// This program is designed to play the game wordle. How the game works is there is a random 5-letter word chosen from
// a file of 5-letter words. Then the user is prompted to enter a 5-letter word as a guess, the program will dissect
// each letter from both the chosen, and the guess word, and if there are matching letters, the user will be notified.
// We will have an array of unused letters the user can pick from, as well as an array of correct letters, and letters
// the user has already guessed and is incorrect.
public class Wordle {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public static ArrayList<Character> unusedLetters = new ArrayList<>();

    public static ArrayList<Character> cLetters = new ArrayList<>();
    public static ArrayList<Character> wrongLetters = new ArrayList<>();

    public static char[] chosenWord;
    public static char[] breakdownUserGuess;
    public static ArrayList<String> words = new ArrayList<>();
    public static String randomWord;
    public static String userGuess;
    public static int attemptsLeft;


    public static String getChosenWord() throws FileNotFoundException {
        // read the txt words file and add all words into an Arraylist
        File file = new File("C:\\Programming\\Java\\Wordle\\src\\words");
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            words.add(line);
        }
        scanner.close();

//        System.out.println(words.toString());

        // get a random word out of the Arraylist
        Random random = new Random();
        int randomArrayIndex = random.nextInt(words.size());
//        System.out.println(randomArrayIndex);
        randomWord = words.get(randomArrayIndex);
        // for development, print the random word
        System.out.println(randomWord);

        initUnusedLetters();
        return randomWord;
    }
    public static ArrayList<Character> initUnusedLetters(){
        for (char ch = 'a'; ch <= 'z'; ch++){
            unusedLetters.add(ch);

        }
//        System.out.println(unusedLetters);
        return unusedLetters;
    }

    public static char[] breakdownChosenWord(String randomWord){
        chosenWord = randomWord.toCharArray();
//        for (char l : chosenWord){
//            System.out.print(l + " ");
//        }
        return chosenWord;

    }

    public static int welcomeUser(){
        attemptsLeft = 5;
        System.out.println("Welcome to Wordle! You have " + attemptsLeft + " attempts left to guess the word correctly!");
        System.out.println("*Yellow means correct letter and spot, Green means correct letter and bad spot, and Red means wrong letter and spot*");

        return attemptsLeft;
    }

    public static String promptUser() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a 5 letter word as a guess: ");
        String userGuessOriginal = sc.next();
        userGuess = userGuessOriginal.toLowerCase();
        while(userGuess.length() != 5){
            System.out.println("Please try again and make sure your guess is 5 characters: ");
            userGuess = sc.next();
        }
        if (userGuess.equals(randomWord)){
            System.out.println(YELLOW + randomWord +  RESET + " You guessed correctly!" + " " + RESET);
            playAgain();
        }

        attemptsLeft--;
        return userGuess;
    }

    public static char[] breakdownGuess(String userGuess){
        breakdownUserGuess = userGuess.toCharArray();
        return breakdownUserGuess;
    }

    public static ArrayList<Character> correctLetters(char x){
        if (cLetters.contains(x)){
            Collections.sort(cLetters);
            return cLetters;
        }
        cLetters.add(x);
        unusedLetters.remove((Character) x);
        Collections.sort(cLetters);
        return cLetters;
    }
    public static ArrayList<Character> badLetters(char x){
        if (cLetters.contains(x)){
            return wrongLetters;
        }
        if (wrongLetters.contains(x)){
            Collections.sort(cLetters);
            return wrongLetters;
        }
        wrongLetters.add(x);
        unusedLetters.remove((Character) x);
        Collections.sort(wrongLetters);
        return wrongLetters;
    }


    public static void compareCharArrays(char[] chosenWord, char[] breakdownGuess){
        ArrayList<Character> chosenWordList = new ArrayList<>();
        for (char c : chosenWord) {
            chosenWordList.add(c);
        }

        for (char guess : breakdownGuess){
            if (chosenWordList.contains(guess)){
                correctLetters(guess);
//                System.out.println(GREEN + "Correct Letter!: " + guess + " " + RESET);
                chosenWordList.remove((Character) guess);
            } else {
                badLetters(guess);
//                System.out.println(RED + "This letter was never found: " + guess + RESET);
            }
        }
    }

    public static void print(){
        System.out.print(GREEN + "\nThese are the correct letters: " + RESET);
        for (char l : cLetters){
            System.out.print(GREEN + l + " " + RESET);
        }
        System.out.print(RED + "\nThese are the wrong letters: " + RESET);
        for (char x : wrongLetters){
            System.out.print(RED + x + " " + RESET);
        }
        System.out.println();
        System.out.print("These letters are unused: ");
        for (char z : unusedLetters){
            System.out.print(z+" ");
        }
        System.out.println();
        // print out the order in which input was given, if letter is wrong then mark it in red, if letter is
        // correct and in the wrong spot, mark it in green, and if letter is correct and in correct spot, the mark
        // it in yellow.
        // breakdownGuess is the original user guess char
        for (int i = 0; i < breakdownUserGuess.length; i++) {
            char guess = breakdownUserGuess[i];
            if (chosenWord[i] == guess) {
                // if letter is correct and in correct spot, mark it in yellow
                System.out.print(YELLOW + guess + " " + RESET);
            } else if (cLetters.contains(guess)) {
                // if letter is correct and in the wrong spot, mark it in green
                System.out.print(GREEN + guess + " " + RESET);
            } else {
                // if letter is wrong then mark it in red
                System.out.print(RED + guess + " " + RESET);
            }
        }

    }

    public static void repeat() throws FileNotFoundException {
        while(attemptsLeft != 0){
            System.out.println("\nYou have " + attemptsLeft + " attempts left");
            promptUser();
            breakdownGuess(userGuess);
            compareCharArrays(chosenWord, breakdownUserGuess);
            print();
        }
        if (attemptsLeft == 0){
            System.out.println();
            System.out.println("The correct word was: " + randomWord);
        }


    }

    public static void playAgain() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Want to play again? Yes or No");
        String userDecision = sc.next();
        if (userDecision.startsWith("y") || userDecision.startsWith("Y")){
            play();
        } else {
            System.out.println("Thank you for playing!");
            System.exit(0);
        }
    }

    public static void play() throws FileNotFoundException {
        getChosenWord();
        breakdownChosenWord(randomWord);
        welcomeUser();
        promptUser();
        breakdownGuess(userGuess);
        compareCharArrays(chosenWord, breakdownUserGuess);
        print();
        repeat();
        playAgain();
    }



    public static void main(String[] args) throws FileNotFoundException {
        play();
    }
}


