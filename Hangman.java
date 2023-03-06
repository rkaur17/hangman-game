import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Hangman {

	public static void main(String [] args) {

		Hangman hangman = new Hangman();

		List<String> phraseList = null;
		// Get the phrase from a file of phrases
		try {
			phraseList = Files.readAllLines(Paths.get("phrases.txt"));
		} catch (IOException e) {
			System.out.println(e);
		}

		//generate a random index to pick the next phrase
		Random random = new Random();
		//generates a random number from zero to the size of the list
		int r = random.nextInt(phraseList.size());
		String nextPhrase = phraseList.get(r);

		//remove extra whitespace from front of nextPhrase
		//replace spaces with '+'
		nextPhrase = nextPhrase.trim();
		StringBuilder hidden = new StringBuilder();
		for (int i = 0; i < nextPhrase.length(); i++) {
			char c = nextPhrase.charAt(i);
			if (Character.isLetter(c)) {
				hidden.append("*");
			} else {
				hidden.append(c);
			}
		}
		//n is the max number of misses
		int n = 5;
		int livesUsed = 0;
		List<String> alreadyGuessed = new ArrayList<>();
		while (livesUsed < n) {
			String guess;
			Scanner scanner = new Scanner(System.in);
			System.out.println("Guess a letter");
			//set the guess equal to the user's guess
			guess = scanner.nextLine();

			//if input is a single letter
			if (guess.length() == 1 && Character.isLetter(guess.charAt(0))) {

				//check if the guess was already guessed
				if (alreadyGuessed.contains(guess)) {
					System.out.println("You've already used this guess.");
				} else {
					//otherwise it is a new guess, add it to the list of guesses
					alreadyGuessed.add(guess);

					//go through the phrase and replace the *'s with the guess
					// if the guess is in the original phrase
					//case should be ignored when checking, but we need to replace with case same as phrase
					if (nextPhrase.toLowerCase().contains(guess.toLowerCase())) {
						for (int i = 0; i < nextPhrase.length(); i++) {
							if(guess.toLowerCase().equals(nextPhrase.substring(i, i + 1).toLowerCase())) {
								hidden.setCharAt(i, nextPhrase.charAt(i));
							}
						}
						System.out.println("Correct!");
						//check to see if you have won - if you have won, close the scanner and break out of the loop
						if(!hidden.toString().contains("*")) {
							System.out.println("Congrats! You win!");
							scanner.close();
							break;
						}
					} else { //if it does not contain the guess
						//add a body part to the noose
						livesUsed++;
						System.out.println("That guess is incorrect!");
					}
				}
			} else {
				System.out.println("That is not a valid entry. You must enter a single letter!");
			}

			System.out.println("What you've guessed so far: " + hidden);
			System.out.println("Here are the guesses you've already used: " + alreadyGuessed);
			System.out.println("Number of misses: " + livesUsed);
		}
		// double check to see if you lost, the loop ends when you lose, but also breaks when you win
		if (livesUsed == n) {
			System.out.println("Sorry! You lose");
			System.out.println("The correct phrase is: " + nextPhrase);
			System.out.println("Your guess so far: " + hidden);
		}
	}

}

