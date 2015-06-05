// Converts an integer from decimal form (base 10) to octal form (base 8)

import java.util.Scanner;

public class convertDecimalToOctal {
	
	static String octalString = "";
	
	public static int octal(int decimalNum) {
		double numDouble = (double) decimalNum;
		
		// You can replace the number 8 with any number less than 10 to convert integers to that number's base.
		numDouble /= 8;
		// For example, if you replaced 8 with 2, you could convert decimal numbers to binary. 
		
		int numInt = (int) numDouble;
		
		// By subtracting the integer from the double, we can get at the portion after the decimal point, 
		// which determines the first octal digit from the right.
		double octalDouble = numDouble - numInt;
		
		// If you changed the number 8 above, replace the number 8 here as well.
		octalDouble *= 8;
		// Using the portion after the decimal point, we can determine the octal equivalent of the number in that place.
		
		// Casting the octal back into an integer removes anything after the decimal point
		int octalInt = (int) octalDouble;
		
		// Placing the newly converted number into a string allows us to convert the decimal number one digit at a time, 
		// from right to left. 
		octalString = octalInt + octalString;
		
		// Recursion: checks if there are still digits in the decimal number that need to be converted to octal form
		// Then calls this method again
		if (numInt >= 1) {
			octal(numInt);
		}
		
		// Casts the octal string as an integer, so it can be returned
		int octal = Integer.parseInt(octalString);		
		return octal;
	}

	public static void main(String[] args) {
		// prompts user for integer
		// converts integer to octal form and prints it to the console
		
		System.out.print("Enter an integer: ");
		Scanner input = new Scanner(System.in);
		int numberInputted = input.nextInt();
		int octalResult = octal(numberInputted);
		System.out.println("Octal equivalent: " + octalResult);
		input.close();
	}

}
