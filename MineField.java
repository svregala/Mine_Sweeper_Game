// Name: Steve Regala
// USC NetID: sregala
// CS 455 PA3
// Fall 2021


import java.util.Arrays;
import java.util.Random;

/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {

   /**
    Representation invariant:
    - numMines has to be greater than or equal to 0, and less than 1/3 of total field locations (row*col)
    - fieldBoard: each row has the same amount of columns, each column has the same amount of rows
   */

   // <put instance variables here>
   private int numberOfMines;
   private boolean[][] fieldBoard;
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
      // first, let fieldBoard have the same number of rows as mineData
      fieldBoard = new boolean[mineData.length][];
      numberOfMines = 0;

      // then, go through each row and of mineData
      // and assign each row in fieldBoard with a copy of a row from mineData
      for (int i=0; i<mineData.length; i++) {
         fieldBoard[i] = Arrays.copyOf(mineData[i], mineData[i].length);
      }

      for (int rows=0; rows<numRows(); rows++) {
         for (int cols=0; cols<numCols(); cols++) {
            if (hasMine(rows,cols)) {
               numberOfMines++;
            }
         }
      }

   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      fieldBoard = new boolean[numRows][numCols];
      numberOfMines = numMines;
      for (int rows=0; rows<numRows(); rows++) {
         for (int cols=0; cols<numCols(); cols++) {
            fieldBoard[rows][cols] = false;
         }
      }

   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {

      Random random = new Random();
      int keepCount = 0;

      // for each row in fieldBoard, go through each column in that row and set that value to false
      resetEmpty();

      // stay in while loop until we've placed numMines() mines in array
      // once keepCount = numMines(), exit while loop
      while (keepCount < numberOfMines) {
         // pick a random spot on the board
         int randRow = random.nextInt(numRows());
         int randCol = random.nextInt(numCols());

         // make sure randomly chosen (row,col) isn't the spot to avoid
         if (randRow != row || randCol != col) {
            // if random spot is false, put the mine there
            if (!fieldBoard[randRow][randCol]) {
               fieldBoard[randRow][randCol] = true;
               keepCount++;  // increase keepCount after placing mines
            }
         }
      }

   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in 
         at the beginning of a game.
    */
   public void resetEmpty() {
      for (int rows=0; rows<numRows(); rows++) {
         for (int cols=0; cols<numCols(); cols++) {
            fieldBoard[rows][cols] = false;
         }
      }

   }

   
   /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {

      int tempRow = row;
      int tempCol = col;

      // case 1, top-left corner of 2D array
      if (row == 0 && col == 0) {
         return numAdjacentHelpCase1(fieldBoard, tempRow, tempCol);
      }

      // case 2, top-right corner of 2D array
      else if (row == 0 && col == numCols()-1) {
         return numAdjacentHelpCase2(fieldBoard, tempRow, tempCol);
      }

      // case 3, bottom-right corner of 2D array
      else if (row == numRows()-1 && col == numCols()-1) {
         return numAdjacentHelpCase3(fieldBoard, tempRow, tempCol);
      }

      // case 4, bottom-left corner of 2D array
      else if (row == numRows()-1 && col == 0) {
         return numAdjacentHelpCase4(fieldBoard, tempRow, tempCol);
       }

      // case 5, any row on the 0th column (except corners)
      else if (col == 0) {
         return numAdjacentHelpCase5(fieldBoard, tempRow, tempCol);
      }

      // case 6, any column on the 0th row (except corners)
      else if (row == 0) {
         return numAdjacentHelpCase6(fieldBoard, tempRow, tempCol);
      }

      // case 7, any column on the last row (except corners)
      else if (row == numRows()-1) {
         return numAdjacentHelpCase7(fieldBoard, tempRow, tempCol);
      }

      // case 8, any row on the last column (except corners)
      else if (col == numCols()-1) {
         return numAdjacentHelpCase8(fieldBoard, tempRow, tempCol);
      }

      // case 9, non-edge spot
      else {
         return numAdjacentHelpCase9(fieldBoard, tempRow, tempCol);
      }

   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      if (row >= 0 && row < numRows()) {
         if (col >= 0 && col < numCols()) {
            return true;
         }
         return false;
      }
      return false;
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return fieldBoard.length;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return fieldBoard[0].length;
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      if (fieldBoard[row][col]) {
         return true;
      }
      return false;
   }

   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return numberOfMines;
   }

   // --------------------------------------------------------------------------------

   /*
   // method to see if instance variables got initialized properly
   // this method will print the 2D array on ONE LINE
   public String toString() {
      return Arrays.deepToString(fieldBoard);
   }
   */

   // this method will print the 2D array on MULTIPLE LINES acordingly
   /**
    This is the toString method that I implemented that we are allowed to have.
    It prints out the formed 2D array on multiple lines accordingly;
    it'll allow me to see if instance variables got initialized properly (in my test case file).
    */
   public void arrayToString() {
      for (int i=0; i< numRows(); i++) {
         for (int j=0; j<numCols(); j++) {
            System.out.print(" " + fieldBoard[i][j] + " ");
         }
         System.out.println();
      }
   }

   // --------------------------------------------------------------------------------


   // <put private methods here>

   // THE FOLLOWING 9 PRIVATE METHODS BELONG TO THE PUBLIC CLASS 'numAdjacentMines(int row, int col)'
   /**
    This is a private helper method representing CASE 1, top-left corner of 2D array.
    Returns the number of mines adjacent to the top-left corner of the 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase1(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col+1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 2, top-right corner of 2D array.
    Returns the number of mines adjacent to the top-right corner of 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase2(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col-1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 3, bottom-right corner of 2D array.
    Returns the number of mines adjacent to the bottom-right corner of 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase3(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row-1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col-1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 4, bottom-left corner of 2D array.
    Returns the number of mines adjacent to the bottom-left corner of 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase4(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row-1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col+1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 5, any row on the 0th column (except corners).
    Returns the number of mines adjacent to any row on the 0th column (except corners) of the 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase5(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row-1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 6, any column on the 0th row (except corners).
    Returns the number of mines adjacent to any column on the 0th row (except corners) of the 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase6(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col+1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 7, any column on the last row (except corners).
    Returns the number of mines adjacent to any column on the last row (except corners) of the 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase7(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col+1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 8, any row on the last column (except corners).
    Returns the number of mines adjacent to any row on the last column (except corners) of the 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase8(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row-1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }

   /**
    This is a private helper method representing CASE 9, non-edge spot.
    Returns the number of mines adjacent to any non-edge spot of the 2D array (not counting a possible
    mine at (row, col) itself).
    @param mineFieldBoard  array that we use to look at particular spots
    @param row  row of the location to check
    @param col  col of the location to check
    PRE: inRange(row, col), mineFieldBoard is the same array used throughout this class
    */
   private int numAdjacentHelpCase9(boolean[][] mineFieldBoard, int row, int col) {

      int numberAdjacentMines = 0;

      if (fieldBoard[row-1][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row-1][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col-1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row][col+1] == true) {
         numberAdjacentMines++;
      }
      if (fieldBoard[row+1][col+1] == true) {
         numberAdjacentMines++;
      }

      return numberAdjacentMines;
   }
   // THE ABOVE ^^^ 9 PRIVATE METHODS BELONG TO THE PUBLIC CLASS 'numAdjacentMines(int row, int col)'


   /**
    Returns true iff the MineField data is in a valid state.
    (See representation invariant comment for more details.)
    */
   private boolean isValidMineField() {

      // check numberOfMines condition
      if (numberOfMines==0 || (numberOfMines>=0 && numberOfMines<((1/3)*numRows()*numCols()) ) ) {
         // check if each row has the same amount of columns
         for (int i=0; i<numRows()-1; i++) {
            if (fieldBoard[i].length != fieldBoard[i+1].length) {
               return false;
            }
         }
         // there's no need to check if each column has the same amount of rows because
         // each row having the same amount of columns implies each column has the same amount of rows
         return true;
      }

      return false;
   }

}