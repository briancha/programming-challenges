import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PlusMinus {

	// Basic linked list implementation
	// Just provides a Node class with which to create Node objects
	static class Node {
		int value;
		Node next;

		public Node(int num) {
			value = num;
		}
	}

	public static void main(String[] args) {
		File input = new File("in.txt");
		Scanner scanner = null;

		try {
			// Uses Scanner class to perform I/O on in.txt
			scanner = new Scanner(input);
		}
		// End program if file is not found
		catch (FileNotFoundException e) {
			System.out.println("Please create an in.txt file in the same folder as this file.");
			return;
		}

		int size = 0;
		Node head = null;
		Node prevNode = null;
		Node currentNode = null;

		// Use the Scanner class and a while loop to take integers from in.txt
		// and place them into a linked list
		while (scanner.hasNextInt()) {
			Node newNode = new Node(scanner.nextInt());

			// Make the first integer the head of the list
			if (size == 0) {
				head = newNode;
				prevNode = newNode;
			} else {
				currentNode = newNode;
				prevNode.next = currentNode;
				prevNode = prevNode.next;
			}

			size++;
		}

		/*
		 * Call the recursive method expression() on the head node to see if the
		 * integers in the linked list can be added/subtracted in a way that
		 * equals any of the integers in the linked list. If so, print "Yes" and
		 * end the program.
		 */
		if (expression(head, head, 0)) {
			System.out.println("Yes");
			return;
		}

		// If no such valid arithmetic expression can be found, print "No"
		System.out.println("No");
	}

	/*
	 * Recursive boolean expression() method: take in the head node, the current
	 * node, and the running total.
	 */
	static boolean expression(Node head, Node current, int runningTotal) {
		/*
		 * Base case: if the current node is empty, iterate through the linked
		 * list with a while loop. If the value at any node is equal to the
		 * running total, return true. If none of the values in the linked list
		 * equal the running total, return false.
		 */
		if (current == null) {
			current = head;

			while (current != null) {
				if (current.value == runningTotal) {
					return true;
				}

				current = current.next;
			}

			return false;
		}

		/*
		 * Recursive case: call expression() on the next node twice 
		 * 1) add the value at the current node to the running total 
		 * 2) subtract the value at the current node from the running total 
		 */
		return (expression(head, current.next, runningTotal + current.value)
				|| expression(head, current.next, runningTotal - current.value));
	}
}