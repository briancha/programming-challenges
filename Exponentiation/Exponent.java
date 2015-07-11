// get the exponent of a number by inputting the base and exponent, i.e. getExponent(3, 2) = 3 ^ 2

public class Exponent {
	
	public static double getExponent(double base, int exponent) {
		double total = base;
		
		// for loop: multiplies the base to itself as many times as the exponent
		for (int i = 1; i < exponent; i++) {
			total *= base;
			// System.out.println(total); // for debugging
		}
		
		// outputs "3 ^ 2 = " to make it clearer to the user
		System.out.print(base + " ^ " + exponent + " = ");
		
		return total;
	}

	public static void main(String[] args) {
		System.out.print(getExponent(8, 5)); // outputs 8.0 ^ 5 = 32768.0
	}

}
