// Takes in a string of parentheses, i.e. ((())()), and returns the number of parentheses pairs there are
// If the pairing of open and closed parentheses is wrong, returns -1 pairs.

public class validParentheses {

	public static int numPairs(String parentheses) {
		int lastChar = parentheses.length() - 1;
		
		// if the first character is a closed parenthesis ")", the program automatically concludes the string of parentheses is invalid. 
		if (parentheses.substring(0, 1).equals(")")) {
			return -1;
		}
		
		// if the last character is an open parenthesis "(", the program automatically concludes the string of parentheses is invalid. 
		if (parentheses.substring(lastChar).equals("(")) {
			return -1;
		}
		
		int openParen = 0, closeParen = 0;
		
		// interates through the string of parentheses, and counts the number of open and closed parentheses 
		for (int i = 0; i < parentheses.length(); i++) {
			if (parentheses.substring(i, i+1).equals("(")) {
				openParen++;
			}
			
			if (parentheses.substring(i, i+1).equals(")")) {
				closeParen++;
			}
		}
		
		// if the number of open and close parentheses are equal, returns the number of parentheses pairs
		if (openParen == closeParen) {
			return closeParen;
		}
		
		// if the numbers are not equal, string of parentheses are invalid and returns -1 pairs.
		return -1;
	}
	
	public static void main(String[] args) {
		// test cases
		System.out.println(numPairs("((()))"));
		System.out.println(numPairs("(()))("));
		System.out.println(numPairs("(()"));
		System.out.println(numPairs("())"));
	}

}
