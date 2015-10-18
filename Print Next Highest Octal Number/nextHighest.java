// Takes in an octal number with 8 different digits and prints out the next highest octal number with 8 different digits  

public class nextHighest {
	public static int nextHighest(int input) {
		int output = 0;
		
		boolean oneOfEachNumber = false;
		boolean no8And9 = false;
		boolean found = false;
		while (!found) {
			int[] numTimesUsed = new int[10];
			
			// increments inputted number
			input++;			
			// converts number to string for easier manipulation
			String inputStr = String.valueOf(input);
			
			// gets numeric value of each digit in the number
			// counts the number of times each digit is used
			for (int i = 0; i < inputStr.length(); i++) {
				int digit = Character.getNumericValue(inputStr.charAt(i));
				numTimesUsed[digit]++;
			}
			
			// looks through numTimesUsed array
			for (int i = 0; i < numTimesUsed.length; i++) {
				
				// for numbers 0-7, makes sure each number has been used once
				if (i >= 0 && i <= 7) {
					if (numTimesUsed[i] == 1) {
						oneOfEachNumber = true;
					} else {
						break;
					}
				}
				
				// for numbers 8 & 9, makes sure the numbers have not been used
				if (i >= 8 && i <= 9){
					if (numTimesUsed[i] == 0) {
						no8And9 = true;
					} else {
						break;
					}
				}
			}
			
			// if numbers 0-7 have been used once and 8 and 9 have not been used at all,
			// breaks out of while loop and returns output
			if (oneOfEachNumber && no8And9) {
				found = true;
				output = input;
			}
		}
		
		return output;
	}
	
	public static void main(String[] args) {
		// test
		System.out.println(nextHighest(74536201));
	}
}
