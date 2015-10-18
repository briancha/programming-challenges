/* From a list of numbers, this program extracts a collection of numbers whose sum is a specific number.
 */

import java.util.ArrayList;

public class calculateCollection {
	public static void main(String[] args) {
		// list of numbers
		int[] originalArray = { 26, 39, 104, 195, 403, 504, 793, 995, 1156, 1673 };
		
		// calls method
		calculateCollection(originalArray);
	}
	
	public static int[] calculateCollection(int[] array) {
		// extracts list of numbers equal to this sum (3165)
		int sum = 3165;
		
		ArrayList arrayList = new ArrayList<>();
		ArrayList numbers = new ArrayList<>();
		
		// adds list of numbers to ArrayList to make it easier to work with
		for (int i : array) {
			arrayList.add(i);
		}	
		
		while (true) {			
			int size = arrayList.size();
			// randomly chooses a position in the array
			int position = (int) (Math.random() * size);
			
			try {
				// takes the value from that random position
				int value = (int) arrayList.get(position);
				
				// subtracts it from the sum
				sum -= value;
				
				// removes number from list, so it does not get subtract it again
				arrayList.remove(position);
				
				// adds this value to numbers ArrayList
				// in case it is part of a collection of numbers whose sum is 3165
				numbers.add(value);
			} 
			
			// for when all numbers have been taken out from the ArrayList
			catch (IndexOutOfBoundsException iobe) {
				// empty numbers ArrayList b/c right collection of numbers have not been found
				numbers.removeAll(numbers);
				break;
			}
			
			// if the sum has been subtracted past 0
			if (sum < 0) {
				// call this method again recursively
				sum = 3165;
				int[] originalArray = { 26, 39, 104, 195, 403, 504, 793, 995, 1156, 1673 };
				calculateCollection(originalArray);
			}
			
			// correct collection of numbers have been found
			// breaks out of while loop
			if (sum == 0) {
				break;
			}			
		}
		
		// takes numbers from ArrayList and stores it in array
		// prints out the right collection of numbers
		int[] numbersArr = new int[numbers.size()];
		for (int i  = 0; i < numbersArr.length; i++) {
			numbersArr[i] = (int) numbers.get(i);
			System.out.print(numbersArr[i] + " ");
		}
		
		// returns the collection of numbers in an array
		return numbersArr;
	}
}
