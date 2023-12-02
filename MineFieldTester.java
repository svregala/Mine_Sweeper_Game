// Name: Steve Regala
// USC NetID: sregala
// CS 455 PA3
// Fall 2021

public class MineFieldTester {

   /*
   MineField Class
   Mutators:
      X- populateMineField
      X- resetEmpty
   Accessors/Constructors:
      X- MineField(boolean[][] mineData)
      X- MineField(int numRows, int numCols, int numMines)
      X- int numAdjacentMines(int row, int col)
      X- boolean inRange(int row, int col)
      X- int numRows()
      X- int numCols()
      X- boolean hasMine(int row, int col)
      X- int numMines()
   */

   public static void main(String[] args) {

      MineField smallField = new MineField(smallMineField);
      MineField emptyField = new MineField(emptyMineField);
      MineField almostEmptyField = new MineField(almostEmptyMineField);

      System.out.println("Dimensions of smallField object:");
      testDimensions(smallField);

      System.out.println("Dimensions of emptyField object:");
      testDimensions(emptyField);

      System.out.println("Dimensions of almostEmptyField object:");
      testDimensions(almostEmptyField);

      System.out.println();
      System.out.println("------------------------------------------------");
      System.out.println("Testing boolean inRange on smallMineField");
      testInRange(smallField);

      System.out.println();
      System.out.println("------------------------------------------------");
      System.out.println("Testing boolean hasMine on smallMineField");
      testHasMine(smallField);

      System.out.println();
      System.out.println("------------------------------------------------");
      System.out.println("Testing boolean hasMine on smallMineField");
      testNumAdjacentMines(smallField);

      // ************************************************************************************************************************

      System.out.println();
      System.out.println("------------------------------------------------");
      System.out.println("Testing 3-arg constructor and numMines() method");
      MineField constructedMineField = new MineField(4,4,3);

      System.out.println("The number of mines in 3-arg constructed MineField is: (Exp 3) " + constructedMineField.numMines());
      System.out.println("The following 2D array is: (Exp: all false)");
      //System.out.print(constructedMineField.toString()); // print out 2D array on one line
      constructedMineField.arrayToString(); // print out 2D array on multiple lines accordingly
      System.out.println();

      System.out.println("Testing populateMineField method (Exp 3 true's in board, except for [1][1] position)");
      constructedMineField.populateMineField(1,1);
      constructedMineField.arrayToString();
      System.out.println();

      System.out.println("Testing resetEmpty method (Exp: all false)");
      constructedMineField.resetEmpty();
      constructedMineField.arrayToString();
      System.out.println();

   }

   private static boolean[][] smallMineField = {
         {false, false, false, false},
         {true, false, false, false},
         {false, true, true, false},
         {false, true, false, true}
   };

   private static boolean[][] emptyMineField = {
         {false, false, false, false},
         {false, false, false, false},
         {false, false, false, false},
         {false, false, false, false}};

   private static boolean[][] almostEmptyMineField = {
         {false, false, false, false},
         {false, false, false, false},
         {false, false, false, false},
         {false, true, false, false}};

   private static void testDimensions(MineField mineFieldObject) {
      System.out.println("Exp: 4 rows & 4 columns");
      System.out.println("Rows: " + mineFieldObject.numRows());
      System.out.println("Columns: " + mineFieldObject.numCols());
   }

   private static void testInRange(MineField mineFieldObject) {
      System.out.println("Exp: true");
      System.out.println("Is row 2, column 3 valid? " + mineFieldObject.inRange(1,2));
      System.out.println();
      System.out.println("Exp: true");
      System.out.println("Is row 4, column 1 valid? " + mineFieldObject.inRange(3,0));
      System.out.println();

      System.out.println("Exp: false");
      System.out.println("Is row -1, column 3 valid? " + mineFieldObject.inRange(-1,2));
      System.out.println();
      System.out.println("Exp: false");
      System.out.println("Is row 5, column 3 valid? " + mineFieldObject.inRange(4,2));
   }

   private static void testHasMine(MineField mineFieldObject) {
      System.out.println("Exp: true");
      System.out.println("Is there a mine at row 2, column 1? " + mineFieldObject.hasMine(1,0));
      System.out.println();

      System.out.println("Exp: true");
      System.out.println("Is there a mine at row 3, column 3? " + mineFieldObject.hasMine(2,2));
      System.out.println();

      System.out.println("Exp: false");
      System.out.println("Is there a mine at row 1, column 3? " + mineFieldObject.hasMine(0,2));
      System.out.println();

      System.out.println("Exp: false");
      System.out.println("Is there a mine at row 2, column 3? " + mineFieldObject.hasMine(1,2));
   }

   private static void testNumAdjacentMines(MineField mineFieldObject) {
      System.out.println("Exp:1 -> Number of adjacent mines at [0,0] is: " + mineFieldObject.numAdjacentMines(0,0));
      System.out.println("Exp:0 -> Number of adjacent mines at [0,3] is: " + mineFieldObject.numAdjacentMines(0,3));
      System.out.println("Exp:1 -> Number of adjacent mines at [3,3] is: " + mineFieldObject.numAdjacentMines(3,3));
      System.out.println("Exp:2 -> Number of adjacent mines at [3,0] is: " + mineFieldObject.numAdjacentMines(3,0));
      System.out.println("Exp:3 -> Number of adjacent mines at [2,0] is: " + mineFieldObject.numAdjacentMines(2,0));
      System.out.println("Exp:1 -> Number of adjacent mines at [0,1] is: " + mineFieldObject.numAdjacentMines(0,1));
      System.out.println("Exp:1 -> Number of adjacent mines at [1,3] is: " + mineFieldObject.numAdjacentMines(1,3));
      System.out.println("Exp:4 -> Number of adjacent mines at [3,2] is: " + mineFieldObject.numAdjacentMines(3,2));
      System.out.println("Exp:3 -> Number of adjacent mines at [1,1] is: " + mineFieldObject.numAdjacentMines(1,1));
   }

}