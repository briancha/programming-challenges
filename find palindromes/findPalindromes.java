// Takes in 2 numbers, and prints out the numbers between them that are palindromes

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class findPalindromes {

	// Takes in 2 numbers
	public static List<Integer> palindromes(int num1, int num2) {
		// Figures out which number is larger and which is smaller
		int greaterNum, smallerNum;
		if (num1 > num2) {
			greaterNum = num1;
			smallerNum = num2;
		} else {
			greaterNum = num2;
			smallerNum = num1;
		}
		
		List<Integer> palindromes = new ArrayList<>();
		
		// iterates through numbers from the smaller to larger number
		for (int i = smallerNum; i < greaterNum; i++) {
			// if the number is a palindrome, it is added to the arraylist palindromes 
			if (isPalindrome(i)) {
				palindromes.add(i);
			}
		}
		
		return palindromes;		
	}
	
	// tests if number is a palindrome
	public static boolean isPalindrome(int number) {
		String numString = "" + number;
		int numLength = numString.length();
		
		// if the first letter is not the same as the last letter, the method immediately returns false.
		if (numString.charAt(0) != numString.charAt(numLength - 1)) {
			return false;
		}
		
		// finds midpoint of number
		int midpoint = numLength / 2;
		
		// creates first and second half
		String firstHalf = numString.substring(0, midpoint);
		String secondHalfTemp = numString.substring(midpoint);
		
		// if the length of the number is odd, the middle digit is ignored 
		if (numLength % 2 != 0) {
			secondHalfTemp = numString.substring(midpoint + 1);
		}
		
		// reverses the second half of the number, so it can be compared with the first half
		String secondHalf = "";
		for (int i = secondHalfTemp.length() - 1; i >= 0; i--) {
			secondHalf += secondHalfTemp.charAt(i);
		}
		
		// returns true if the first half has the same numbers as the second half of the number
		return firstHalf.equals(secondHalf);
	}

	public static void main(String[] args) {
		// prompts user for 2 numbers and outputs the palindromes between them
				
		Scanner input = new Scanner(System.in);	
		System.out.println("Find palindromes between 2 numbers: ");
		System.out.print("Enter the first number: ");
		int firstNum = input.nextInt();
		System.out.print("Enter the second number: ");
		int secondNum = input.nextInt();
		List<Integer> palindromes = palindromes(firstNum, secondNum);
		System.out.println(palindromes);
	}

}
