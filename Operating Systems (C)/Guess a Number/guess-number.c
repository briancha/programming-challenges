#include <stdio.h>
#include <stdlib.h>

// Arrays to store the number of passes and consecutive passes 
// each player has made
int numPasses[3];
int numConsecutivePasses[3];

// Function declarations (used later on in the program)
void promptForGuess(int playerNum, int generatedNum);
int checkAccuracy(int guess, int playerNum, int generatedNum);

// Main Guess_My_Number
int main(void) {
	// Disables buffering
	setbuf(stdout, NULL);

	// Randomly choose which player will go first (player 1 or 2)
	int randomPlayer = (rand() % 2) + 1;

	// Prompt player for number
	int playerNum;
	printf("Player Number ");
	scanf("%d", &playerNum);

	// While the right player's number has not been entered, keep prompting 
  // the player until the one randomly chosen earlier enters their number.
	while (randomPlayer != playerNum) {
		printf("You have to wait your turn.\n");
		printf("Player Number ");
		scanf("%d", &playerNum);
	}

	// Generate a random number from 1-100
	int generatedNum = (rand() % 100) + 1;
	printf("Let the guessing begin!\n");

	// Call Procedure promptForGuess, which takes in the player's number 
  // and generated number from 1-100
	promptForGuess(playerNum, generatedNum);
}

// Procedure promptForGuess
void promptForGuess(int playerNum, int generatedNum) {
	char guessStr[4];
	char *guessChar;
	long guessLong;
  int guess;

	// Prompt the player for their guess from 1-100
	printf("Player %d, enter your guess from 1-100: ", playerNum);
	
  // Store their input in a string
  scanf("%s", &guessStr);

  // If the string is equal to "PASS", set the guessed number to -1.
	if (strcmp(guessStr, "PASS") == 0) {
		guess = -1;
	}

	// Else, convert the guessed string to an integer
	else {
    guessLong = strtol(guessStr, &guessChar, 10);
    guess = (int) guessLong;
	}

	// Call Procedure checkAccuracy, which takes in the guess, player's number, 
  // and the computer generated number from 1-100
	checkAccuracy(guess, playerNum, generatedNum);
}

// Procedure checkAccuracy
int checkAccuracy(int guess, int playerNum, int generatedNum) {
	// Figure out who the other player is by getting the number other than 1 or 2
	int otherPlayer;
	if (playerNum == 1) {
		otherPlayer = 2;
	} else {
		otherPlayer = 1;
	}

	// If the player chose to pass
	if (guess == -1) {
		// Increment the number of passes and consecutive passes for that player
		numPasses[playerNum]++;
		numConsecutivePasses[playerNum]++;

		// Calculates the number of passes the player has left
		int passesLeft = 3 - numPasses[playerNum];

		// If a player passes 2 turns in a row, they forfeit and the game ends
		if (numConsecutivePasses[playerNum] == 2) {
			printf("You have passed 2 turns in a row. Player %d forfeits. Player %d wins! The number was %d.\n",
					playerNum, otherPlayer, generatedNum);

			// returns 0 to end the program
			return 0;
		}

		// If a player has passed more than 3 turns total, they forfeit and the game ends
		if (numPasses[playerNum] > 3) {
			printf("You have passed more than 3 times. Player %d forfeits. Player %d wins! The number was %d.\n",
					playerNum, otherPlayer, generatedNum);

			// returns 0 to end the program
			return 0;
		}

		// Show the player how many passes they have used and how many passes they have left
		printf("You have passed %d time(s), you have %d more time(s) left.\n", numPasses[playerNum], passesLeft);

		// Calls promptForGuess for the other player, inputting the same generated number
		promptForGuess(otherPlayer, generatedNum);
	}

	// Else if the player's guess is right, they win and the game ends.
	else if (guess == generatedNum) {
		printf("Your guess was correct! Congratulations, player %d wins!\n", playerNum);
	}

	// Else if the player's guess was higher than the generated number
	else if (guess > generatedNum) {
		printf("Your guess was too high.\n");
		
    // Set number of consecutive passes to 0
    numConsecutivePasses[playerNum] = 0;
		
    // Call promptForGuess for the other player
    promptForGuess(otherPlayer, generatedNum);
	}

	// Else if the player's guess was lower than the generated number
	else {
		printf("Your guess was too low.\n");
		
    // Set number of consecutive passes to 0
    numConsecutivePasses[playerNum] = 0;
		
    // Call promptForGuess for the other player
    promptForGuess(otherPlayer, generatedNum);
	}

	return 0;
}
