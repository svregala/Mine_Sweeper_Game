// Name: Steve Regala
// USC NetID: sregala
// CS 455 PA3
// Fall 2021

import javax.swing.JFrame;

public class VisibleFieldTester{

   /*
   Test Accessors
   - getStatus(row,col)
   - numMinesLeft()
   - isGameOver()

   Test Mutators
   - resetGameDisplay()
   - cycleGuess()
   - uncover()
   * */

   public static void main(String[] args) {

      fixedMineField();

      //randomMineField();

   }

   private static final int FRAME_WIDTH = 400;
   private static final int FRAME_HEIGHT = 425;

   private static int SIDE_LENGTH_ROWS = 12;
   private static int SIDE_LENGTH_COLS = 10;
   private static int NUM_MINES = 12;

   private static boolean[][] exampleMineField = {
               {false, true, false, true},
               {true, false, false, false},
               {false, true, false, false},
               {false, true, false, true},
               {false, false, true, false},
               {false, false, false, false}     // 6x4, 7 mines
   };

   private static void fixedMineField() {

      JFrame frame = new JFrame();

      frame.setTitle("Minesweeper");

      frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

      GameBoardPanel gameBoard = new GameBoardPanel(new VisibleField(new MineField(exampleMineField)));

      frame.add(gameBoard);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.setVisible(true);

   }

   private static void randomMineField() {

      JFrame frame = new JFrame();

      frame.setTitle("Minesweeper");

      frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

      GameBoardPanel gameBoard = new GameBoardPanel(SIDE_LENGTH_ROWS, SIDE_LENGTH_COLS, NUM_MINES);

      frame.add(gameBoard);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.setVisible(true);

   }


}