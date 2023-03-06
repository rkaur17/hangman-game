import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Hangman3 {

    // provide the same methods as in version 2,
    // but also define data members for "phrase", "hiddenPhrase", and "previousGuesses".
    // You will have fewer parameters in your methods for this version.
    public static StringBuilder hiddenPhrase = new StringBuilder();
    public static String phrase;
    public static List<String> previousGuesses = new ArrayList<>();


    public static void main(String[] args) {

        Hangman hangman = new Hangman();

        List<String> phraseList = null;
        // Get the phrase from a file of phrases
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }

        phrase = randomPhrase(phraseList);
        phrase = phrase.trim();
        hiddenPhrase.append(generateHiddenPhrase());

        int livesUsed = 0;

        int n = 10;
        while (livesUsed < n) {
            String guess;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Guess a letter");
            guess = getGuess(scanner);
            if (guess.length() == 1 && Character.isLetter(guess.charAt(0))) {
                if (previousGuesses.contains(guess)) {
                    System.out.println("You've already used this guess.");
                } else {
                    boolean matches = processGuess(guess);
                    if (matches) {
                        System.out.println("Correct!");
                        //check to see if you have won - if you have won, close the scanner and break out of the loop
                        if (!hiddenPhrase.toString().contains("*")) {
                            System.out.println("Congrats! You win!");
                            scanner.close();
                            break;
                        }
                    } else {
                        //add a body part to the noose
                        livesUsed++;
                        System.out.println("That guess is incorrect!");
                    }
                }
            } else {
                System.out.println("That is not a valid entry. You must enter a single letter!");
            }

            System.out.println("What you've guessed so far: " + hiddenPhrase);
            System.out.println("Here are the guesses you've already used: " + previousGuesses);
            System.out.println("Number of misses: " + livesUsed);
        }
        // double check to see if you lost, the loop ends when you lose, but also breaks when you win
        if (livesUsed == n) {
            System.out.println("Sorry! You lose");
            System.out.println("The correct phrase is: " + phrase);
            System.out.println("Your guess so far: " + hiddenPhrase);
        }
    }

    // randomPhrase -- returns a single phrase randomly chosen from a list
    public static String randomPhrase(List<String> phraseList) {
        //generate a random index to pick the next phrase
        Random random = new Random();
        //generates a random number from zero to the size of the list
        int r = random.nextInt(phraseList.size());
        String nextPhrase = phraseList.get(r);
        return nextPhrase;
    }

    // generateHiddenPhrase -- returns the initial hidden phrase
    public static String generateHiddenPhrase() {
        //remove extra whitespace from front of nextPhrase
        //replace spaces with '+'
        String hidden = "";
        for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);
            if (Character.isLetter(c)) {
                hidden += "*";
            } else {
                hidden += c;
            }
        }
        return hidden;
    }

    // getGuess-- gets input from user and returns it.
    public static String getGuess(Scanner scanner) {
        //set the guess equal to the user's guess
        return scanner.nextLine();
    }

    //processGuess -- returns whether a letter matches and modifies the partially hidden phrase, and modifies the hidden phrase if there is a match.
    public static boolean processGuess(String guess) {
        //it is a new guess, add it to the list of guesses
        previousGuesses.add(guess);

        //go through the phrase and replace the *'s with the guess
        // if the guess is in the original phrase
        //case should be ignored when checking, but we need to replace with case same as phrase
        if (phrase.toLowerCase().contains(guess.toLowerCase())) {
            for (int i = 0; i < phrase.length(); i++) {
                if(guess.toLowerCase().equals(phrase.substring(i, i + 1).toLowerCase())) {
                    hiddenPhrase.setCharAt(i, phrase.charAt(i));
                }
            }
            return true;
        } else { //if it does not contain the guess
            return false;
        }
    }
}




