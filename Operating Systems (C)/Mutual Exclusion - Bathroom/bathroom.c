#include <stdio.h>
#include <stdlib.h>

// Methods used in this program
void woman_wants_to_enter();
void man_wants_to_enter();
void woman_leaves();
void man_leaves();
void prompt_user();
void output_status(int inQueue, int position, int gender);

// Set constants for men and women
// To be used for boolean variables bathroomGender and queueGender
const int women = 0;
const int men = 1;

// Global variables to keep track of program, bathroom, and queue status
int cycleCount = 0;
int bathroomGender = 0;
int menInBathroom = 0;
int womenInBathroom = 0;
int menInQueue = 0;
int womenInQueue = 0;
int queueGender = 0;

int main(void) {
  // Call prompt_user 
  prompt_user();
}

// Procedure woman_wants_to_enter
void woman_wants_to_enter() {
  // Increment cycleCount
  cycleCount++;
  
  // If there are no men in the bathroom or there are less than 10 women in the bathroom
  if (menInBathroom == 0 && womenInBathroom < 10) {
    // Allow the woman to go to bathroom
    womenInBathroom++;
    // The bathroom is now only allowed for women
    bathroomGender = women;
    // All men that need to go to the bathroom must wait in the queue
    queueGender = men;
    // Call output_status with placeholder parameters
    output_status(-1, -1, -1);
  }
  
  // Else, the woman must wait
  else {
    womenInQueue++;
    // Call output_status, indicating the woman is in the queue, her position, and that she is a woman
    output_status(1, womenInQueue, women);
  }
  
  
}

// Procedure man_wants_to_enter
void man_wants_to_enter() {
  // Increment cycleCount
  cycleCount++;
  
  // If there are no women in the bathroom or there are less than 10 men in the bathroom
  if (womenInBathroom == 0 && menInBathroom < 10) {
    // Allow man to go to bathroom
    menInBathroom++;
    // The bathroom is now only allowed for men
    bathroomGender = men;
    // All women must wait in the queue
    queueGender = women;
    // Call output_status with placeholder parameters
    output_status(-1, -1, -1);
  } 
  
  // Else, the man must wait
  else {
    menInQueue++;
    // Call output_status, indicating the man is in the queue, his position, and that he is a man
    output_status(1, menInQueue, men);
  }
  
  
}

// Procedure woman_leaves
void woman_leaves() {
  // If there are no women in the bathroom to begin with, 
  // it is impossible for a woman to leave. 
  // Reprompt user 
  if (womenInBathroom == 0) {
    printf("There are no women in the bathroom.\n\n");
    prompt_user();
  } 
  
  // Else, if there are women in the bathroom
  else {
    // Increment cycleCount
    cycleCount++;
    // Reduce womenInBathroom by 1
    womenInBathroom--;
    
    // If women are in the bathroom
    if (bathroomGender == women) {
      // If there are women in the queue
      if (womenInQueue > 0) {
        // Allow one woman into the bathroom        
        womenInBathroom++;
        // Reduce the women in the queue by 1
        womenInQueue--;
      }
      
      // Else if all women have left the bathroom
      else if (womenInBathroom == 0) {
        // The men are allowed to use the bathroom
        bathroomGender = men;
        // Women must wait in the queue
        queueGender = women;
        
        // If there are 10 or fewer men in the queue
        if (menInQueue <= 10) {
          // All men in the queue can use the bathroom
          menInBathroom = menInQueue;
          // No one is left in the queue
          menInQueue = 0;
        } 
        
        // Else, if there are more than 10 men in the queue
        else {
          // 10 men are allowed into the bathroom
          menInBathroom = 10;
          // There are 10 fewer men in the queue
          menInQueue -= 10;
        }
      }
    }
    
    // Call output_status with placeholder parameters
    output_status(-1, -1, -1);
  }
}

// Procedure man_leaves
void man_leaves() {
  // If there are no men in the bathroom to begin with, 
  // it is impossible for a man to leave.
  // Reprompt user 
  if (menInBathroom == 0) {
    printf("There are no men in the bathroom.\n\n");
    prompt_user();
  } 
  
  // Else, if there are men in the bathroom
  else {
    // Increment cycleCount
    cycleCount++;
    // Reduce menInBathroom by 1
    menInBathroom--;
    
    // If men are in the bathroom
    if (bathroomGender == men) {
      // If there are men in the queue
      if (menInQueue > 0) {
        // Allow one man to enter the bathroom
        menInBathroom++;
        // Reduce the men in the queue by 1
        menInQueue--;        
      }
      
      // Else if all men have left the bathroom
      else if (menInBathroom == 0) {
        // The women are allowed to use the bathroom
        bathroomGender = women;
        // Men must wait in the queue
        queueGender = men;
        
        // If there are 10 or fewer women in the queue
        if (womenInQueue <= 10) {
          // All women in the queue can use the bathroom
          womenInBathroom = womenInQueue;
          // No one is left in the queue
          womenInQueue = 0;
        } 
        
        // Else, if there are more than 10 women in the queue
        else {
          // 10 women are allowed into the bathroom
          womenInBathroom = 10;
          // There are 10 fewer women in the queue
          womenInQueue -= 10;
        }
      }
    }
    
    // Call output_status with placeholder parameters
    output_status(-1, -1, -1);
  }
}

// Procedure output_status
void output_status(int inQueue, int position, int gender) {
  // Displays the number of the cycle
  printf("Cycle %d: The bathroom is ", cycleCount);
  
  // If bathroom is unoccupied, display that it is empty
  if (menInBathroom == 0 && womenInBathroom == 0) {
    printf("empty. ");
  }
  
  // Else, displays the state of the bathroom (number of men or women)
  // Determine whether to use singular or plural version of men/women for bathroom
  else {
    char plural = 'e';
    
    if (bathroomGender == women) {
      if (womenInBathroom == 1) {
        plural = 'a';
      }
      printf("occupied by %d wom%cn. ", womenInBathroom, plural);
    } else {
      if (menInBathroom == 1) {
        plural = 'a';
      }
      printf("occupied by %d m%cn. ", menInBathroom, plural);
    }
  }
  
  // Determine whether to use singular or plural version of men/women for queue
  char menPlural = 'e';
  char womenPlural = 'e';
  if (menInQueue == 1) {
    menPlural = 'a';
  }
  if (womenInQueue == 1) {
    womenPlural = 'a';
  }
  
  // Displays the status of the queue (number of men or women)
  printf("The queue ");
  if (menInQueue == 0 && womenInQueue == 0) {
    printf("is empty");
  }
  else if (menInQueue == 0) {
    printf("has %d wom%cn", womenInQueue, womenPlural);
  }
  else if (womenInQueue == 0) {
    printf("has %d m%cn", menInQueue, menPlural);
  }
  else {
    printf("has %d wom%cn and %d m%cn", womenInQueue, womenPlural, menInQueue, menPlural);
  }
  
  // If the person was trying to enter the bathroom but has to wait in the queue,
  // say what position in their gender's queue they are in.
  if (inQueue == 1) {
    char woman[] = "woman";
    char man[] = "man";
    if (gender == men) {
      printf(". He is the #%d %s in line", position, man);
    } else {
      printf(". She is the #%d %s in line", position, woman);
    }
  }
  
  printf(".\n\n");
  
  // Call prompt_user
  prompt_user();
}

// Procedure prompt_user
void prompt_user() {
  // Prompts user to generate each new person entering the queue, entering the bathroom, or leaving the bathroom
  // User chooses by picking a number from 1-4
  printf("Who needs to go to or leave the bathroom?\n1) woman needs to enter\n2) man needs to enter\n3) woman needs to leave\n4) man needs to leave\nEnter a number from 1-4: ");
  int enteredNum = 0;
  // Takes in user choice
  scanf("%d", &enteredNum);
  
  // Calls woman_wants_to_enter
  if (enteredNum == 1) {
    woman_wants_to_enter();
  } 
  // Calls man_wants_to_enter
  else if (enteredNum == 2) {
    man_wants_to_enter();
  } 
  // Calls woman_leaves
  else if (enteredNum == 3) {
    woman_leaves();
  } 
  // Calls man_leaves
  else if (enteredNum == 4) {
    man_leaves();
  } 
  // The user picked a number that was not 1-4
  // User is reprompted for choice
  else {
    printf("Invalid choice. Please try again.\n\n");
    prompt_user();
  }
}