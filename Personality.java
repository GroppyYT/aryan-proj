
// Tim Liu
// 2/16/16
// TCSS 143
// Assignment #5
//
// This program will convert A and B answers from a 
// Keirsey Temperament Sorter from a text file and to a 
// four-letter personality type that will be written new output file.

import java.io.*;
import java.util.*;

public class Personality {

	// Initiate constant for the dimensions for readability, though changing
	// this will break the program.
	public static final int DIMENSION = 4;

	public static void main(String[] args) throws FileNotFoundException {

		Scanner input = new Scanner(new File("personality"));
		PrintStream output = new PrintStream(new File("personality.out"));

		// Read through every line in the file
		while (input.hasNextLine()) {

			// String answers will read through only the answers
			String name = input.nextLine();
			String answers = input.nextLine();
			answers = answers.toUpperCase();

			char[] charArray = answers.toCharArray();
			int[] aChoices = getA(charArray);
			int[] bChoices = getB(charArray);
			int[] percentage = getPercent(aChoices, bChoices);

			String[] personalityType = getPersonalityType(percentage);

			// Write output into file personality.out
			printOutput(output, name, percentage, personalityType);

		}
		
		input.close();
		output.close();

	}

	// Takes in the char array and scans through each answer line to count the
	// A's in each dimension,
	// returns integer array with A answers.
	public static int[] getA(char[] charArray) {

		int[] A = new int[DIMENSION];

		for (int i = 0; i < charArray.length; i++) {
			char letter = charArray[i];

			if (letter == 'A' || letter == 'a') {

				if (i % 7 == 0) {
					A[0]++;
				}

				if (i % 7 == 1 || i % 7 == 2) {
					A[1]++;
				}

				if (i % 7 == 3 || i % 7 == 4) {
					A[2]++;
				}

				if (i % 7 == 5 || i % 7 == 6) {
					A[3]++;
				}
			}
		}

		return A;

	}

	// Counts the B's in each dimension, returns integer array with B answers.
	public static int[] getB(char[] charArray) {

		int[] B = new int[DIMENSION];

		for (int i = 0; i < charArray.length; i++) {
			char letter = charArray[i];

			if (letter == 'B' || letter == 'b') {

				if (i % 7 == 0) {
					B[0]++;
				}

				if (i % 7 == 1 || i % 7 == 2) {
					B[1]++;
				}

				if (i % 7 == 3 || i % 7 == 4) {
					B[2]++;
				}

				if (i % 7 == 5 || i % 7 == 6) {
					B[3]++;
				}
			}
		}

		return B;

	}

	// Takes in array of A and B answers, calculates the percentage of A and B
	// answers for each dimension,
	// returns an integer array with percents.
	public static int[] getPercent(int[] A, int[] B) {

		int percent[] = new int[DIMENSION];

		for (int i = 0; i < percent.length; i++) {
			double math = (B[i] * 100.0 / (B[i] + A[i]));
			int count = (int) Math.round(math);
			percent[i] = count;

		}

		return percent;

	}

	// Takes in the percent array, determine the 4-letter personality type
	// and returns that as a string array.
	public static String[] getPersonalityType(int[] percent) {

		String[] personality = new String[DIMENSION];

		for (int i = 0; i < personality.length; i++) {

			if (percent[i] > 50) {
				if (i == 0) {
					personality[0] = "I";

				} else if (i == 1) {
					personality[1] = "N";

				} else if (i == 2) {
					personality[2] = "F";

				} else {
					personality[3] = "P";
				}

			} else if (percent[i] < 50) {

				if (i == 0) {
					personality[0] = "E";

				} else if (i == 1) {
					personality[1] = "S";

				} else if (i == 2) {
					personality[2] = "T";

				} else {
					personality[3] = "J";
				}

			} else if (percent[i] == 50) {
				personality[i] = "X";
			}
		}

		return personality;

	}

	// Writes the personality percentages and results into an output file
	public static void printOutput(PrintStream output, String line, int[] percent, String[] personality) {

		System.out.print(line + ": ");
		System.out.print(Arrays.toString(percent) + " = ");

		// Print personality trait so that ['I', 'N', 'F', 'P'] becomes INFP
		for (int i = 0; i < personality.length; i++) {
			System.out.print((personality[i]));
		}

		System.out.println();

	}

}
