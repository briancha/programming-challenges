# Solitaire
* Command line version of popular Solitaire game (1-card draw)
* Use the keyboard to draw cards, move cards to the tableau piles or a foundation stack, or undo a draw
* Choose a card by entering its name on the command line (case insensitive), i.e. "AD" for ace of diamonds
* Adds/subtracts points to your score based on certain actions in the game, i.e. adding a drawn card to a tableau pile, adding a card to a foundation stack, restarting the draw pile, revealing a new card in a tableau pile
* If a game can be won (all tableau cards are face up and draw piles and drawn cards piles are empty), offers an autocomplete feature to add all the tableau cards to the foundation stacks automatically and win the game for you.
* Keeps track of the top 10 recorded scores and notifies you if you crack the top 10
  * Stores scores on an Excel spreadsheet (Apache POI) for later retrieval
  
Sample Game:
```
Drawn:  3D          Foundation Stacks:  AH       Points: 20
 1    2    3    4    5    6    7
     ---  ---  ---  ---  ---  ---
      6S  ---   QH  ---  ---  ---
           5C       ---  ---  ---
                    ---  ---  ---
                     5D  ---  ---
                     4S   QS  ---
                               3S
                               2D

Would you like to draw, or move a card to a tableau pile or to a foundation
stack? 1) draw 2) tableau 3) foundation 4) exit 5) restart 6) undo last draw
2

Which card, i.e. "2D"? Type "c" to cancel.
3d

 1    2    3    4    5    6    7
     ---  ---  ---  ---  ---  ---
      6S  ---   QH  ---  ---  ---
           5C       ---  ---  ---
                    ---  ---  ---
                     5D  ---  ---
                     4S   QS  ---
                               3S
                               2D

Which pile do you want to move 3D to? Choose a pile from 1-7.
Type "c" to cancel.
5

Moved 3D from draw pile to tableau. Points +5.
3D moved successfully.

Drawn:  2H          Foundation Stacks:  AH       Points: 25
 1    2    3    4    5    6    7
     ---  ---  ---  ---  ---  ---
      6S  ---   QH  ---  ---  ---
           5C       ---  ---  ---
                    ---  ---  ---
                     5D  ---  ---
                     4S   QS  ---
                     3D        3S
                               2D

Would you like to draw, or move a card to a tableau pile or to a foundation
stack? 1) draw 2) tableau 3) foundation 4) exit 5) restart 6) undo last draw
```