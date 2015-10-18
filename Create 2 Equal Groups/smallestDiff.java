// From a set of numbers, this program creates 2 sets of numbers with the smallest difference in values

import java.util.Arrays;

public class smallestDiff {
	public static int[][] smallestDiff(int[] packages) {
		// sort array of packages using quicksort (O(nlogn))
		Arrays.sort(packages);
		
		int length = packages.length;
		int median = 0;
		int medianIndex = length / 2; // middle position of array
		
		// 2 arrays for 2 groups of pkgs
		int[] oddGroup = new int[medianIndex];
		int[] evenGroup = new int[medianIndex];
		
		// if package array is odd
		if (length % 2 != 0) {
			// set median to middle element
			median = packages[medianIndex];
			// add 1 more position to one of the 2 arrays for the extra number
			evenGroup = new int[medianIndex + 1];
		} 
		// if package array is even
		else {
			// median = average of middle 2 elements
			median = (packages[medianIndex] + packages[medianIndex - 1]) / 2;			
		}
		
		int evenCount = 0;
		int oddCount = 0;
		int end = length - 1;
		// iterate through sorted packages array
		for (int i = 0; i < packages.length; i++) {
			try {
				// add first and last elements of sorted array 
				// to either first or second group of packages
				// depending on whether the cycle in the for loop is odd/even
				// until all elements from packages array have been added
				if (i % 2 == 0) {
					evenGroup[evenCount] = packages[i];
					evenCount++;
					evenGroup[evenCount] = packages[end];
					evenCount++;
					end--;
				} else {
					oddGroup[oddCount] = packages[i];
					oddCount++;
					oddGroup[oddCount] = packages[end];
					oddCount++;
					end--;
				}
			} catch (ArrayIndexOutOfBoundsException aiobe) {
				// if loop tries to add more elements to one of the 2 arrays than is possible
			}
		}
		
		// returns both groups of packages as an array of 2 arrays
		int[][] bothGroups = {oddGroup, evenGroup};		
		return bothGroups;		
	}
	
	public static void main(String[] args) {
		// test set of packages
		int[] packages = {12, 5, 2, 8, 7, 6, 9, 11, 4, 15};
		int[][] groups = smallestDiff(packages);
		
		// prints out both groups of packages
		for (int array = 0; array < groups.length; array++) {
			System.out.print("Group " + array + ": ");
			int[] group = groups[array];
			for (int value = 0; value < groups[array].length; value++) {
				System.out.print(group[value] + " ");
			}
			System.out.println();
		}
	}
}
