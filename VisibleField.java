// Name: Steve Regala
// USC NetID: sregala
// CS 455 PA3
// Fall 2021


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield). Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {

   /**
    Representation invariant:
    - squareStatus must match the same dimensions as the passed-in MineField
    */

   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method
   // getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   

   // <put instance variables here>
   private MineField officialMineField;
   private int[][] squareStatus; // 2D array representation of status for each square


   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
      officialMineField = mineField;

      // populate 2D array with values COVERED
      squareStatus = new int[officialMineField.numRows()][officialMineField.numCols()];
      for (int rows = 0; rows < officialMineField.numRows(); rows++) {
         for (int cols = 0; cols < officialMineField.numCols(); cols++) {
            squareStatus[rows][cols] = COVERED;
         }
      }

   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      for (int rows = 0; rows < officialMineField.numRows(); rows++) {
         for (int cols = 0; cols < officialMineField.numCols(); cols++) {
            squareStatus[rows][cols] = COVERED;
         }
      }

   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return officialMineField;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return squareStatus[row][col];
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {

      int tempNumMines = officialMineField.numMines();
      int counter = 0;  // count to subtract from tempNumMines

      for (int rows = 0; rows < officialMineField.numRows(); rows++) {
         for (int cols = 0; cols < officialMineField.numCols(); cols++) {
            if (squareStatus[rows][cols] == MINE_GUESS) {
               counter++;
            }
         }
      }

      return tempNumMines-counter;
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {

      if (squareStatus[row][col] == COVERED) {
         squareStatus[row][col] = MINE_GUESS;
      }

      else if (squareStatus[row][col] == MINE_GUESS) {
         squareStatus[row][col] = QUESTION;
      }

      else if (squareStatus[row][col] == QUESTION){
         squareStatus[row][col] = COVERED;
      }

      else {
         return;
      }

   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {

      if (officialMineField.hasMine(row,col)) {
         squareStatus[row][col] = EXPLODED_MINE;
         return false;
      }

      // recursive helper to uncover squares in the neighboring area that are also
      // not next to any mines, it will also uncover any mine-adjacent squares that is reached
      floodFillHelper(row,col);
      return true;

   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {

      int tempCount = 0;

      // check for loss condition first because we increment tempCount
      for (int rows=0; rows<officialMineField.numRows(); rows++) {
         for (int cols=0; cols<officialMineField.numCols(); cols++) {
            if (squareStatus[rows][cols] == EXPLODED_MINE) {
               lossConditionHelper();
               return true;
            }
            if (squareStatus[rows][cols] >= 0) {   //status is non-negative
               tempCount++;   // if a square is correctly uncovered, increment tempCount
            }
         }
      }

      // then check for win condition
      // once tempCount is reaches the same amount as the board count minus number of mines, game is won
      if (tempCount == (officialMineField.numRows()*officialMineField.numCols() - officialMineField.numMines())) {
         winConditionHelper();
         return true;
      }

      return false;
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      if (squareStatus[row][col] >= 0) {
         return true;
      }

      return false;
   }
   
 
   // <put private methods here>

   /**
    This is the lossConditionHelper method seen in isGameOver(). Once an exploded mine is encountered, this method is called.
    It turns covered mines into MINE (black), incorrectly guessed mines into INCORRECT_GUESS (X on top of the yellow),
    correct mine guesses stay (yellow), and question marks on mines will turn to MINE (black).
    */
   private void lossConditionHelper() {
      for (int rows=0; rows<officialMineField.numRows(); rows++) {
         for (int cols=0; cols<officialMineField.numCols(); cols++) {
            // if it is covered and there exists a mine
            if (squareStatus[rows][cols] == COVERED && officialMineField.hasMine(rows,cols)) {
               squareStatus[rows][cols] = MINE;
            }
            // if it is an incorrectly guessed as a mine
            else if (squareStatus[rows][cols] == MINE_GUESS && !officialMineField.hasMine(rows,cols)) {
               squareStatus[rows][cols] = INCORRECT_GUESS;
            }
            // if it has a question mark on it and has a mine on it
            else if (squareStatus[rows][cols] == QUESTION && officialMineField.hasMine(rows,cols)) {
               squareStatus[rows][cols] = MINE;
            }
         }
      }
   }

   /**
    This is the winConditionHelper method seen in isGameOver(). Once it reaches the correct amount of uncovered mines,
    this method is called. After all non-mines have been guessed correctly, this will turn all the squares with mines
    to MINE_GUESS (yellow).
    */
   private void winConditionHelper() {
      for (int rows=0; rows<officialMineField.numRows(); rows++) {
         for (int cols=0; cols<officialMineField.numCols(); cols++) {
            if (officialMineField.hasMine(rows,cols)) {
               squareStatus[rows][cols] = MINE_GUESS;
            }
         }
      }
   }


   /**
    This is the recursive helper method seen in uncover(int row, int col) method. This private method helper
    will uncover squares in the neighboring area that are also not next to any mines. It will also uncover
    any mine-adjacent squares that is reached.
    @param row  of the square
    @param col  of the square
    PRE: getMineField().inRange(row, col)
    */
   private void floodFillHelper(int row, int col) {

      // fillable conditions:
      // if its inrange, not a mine itself, number of adjacent mines is >=0, either covered or question mark on it
      if (officialMineField.inRange(row, col) && officialMineField.numAdjacentMines(row, col) >= 0 &&
            !officialMineField.hasMine(row, col) && (squareStatus[row][col]==COVERED || squareStatus[row][col]==QUESTION)) {

         squareStatus[row][col] = officialMineField.numAdjacentMines(row, col);

         // base case, if a square has mines next to it, don't uncover further squares
         // don't proceed to uncover squares adjacent to squares that have
         // a positive number on it, in other words; don't uncover mines
         if (squareStatus[row][col] > 0) {
            return;
         }

         floodFillHelper(row - 1, col - 1);
         floodFillHelper(row - 1, col);
         floodFillHelper(row - 1, col + 1);
         floodFillHelper(row, col + 1);
         floodFillHelper(row + 1, col + 1);
         floodFillHelper(row + 1, col);
         floodFillHelper(row + 1, col - 1);
         floodFillHelper(row, col - 1);
      }

      return;
   }

   /**
    Returns true iff the VisibleField data is in a valid state.
    (See representation invariant comment for more details.)
    */
   private boolean isValidVisibleField() {

      if (squareStatus.length == officialMineField.numRows()) {
         if (squareStatus[0].length == officialMineField.numCols()) {
            return true;
         }
         return false;
      }

      return true;
   }

}
