/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Model;

import java.util.Random;

/**
 *
 * @author aleks
 */
public class MineField {

    private int numberOfMines; // number of mines on the board in percent of board size
    private int numberOfRows;
    private int numberOfCols;
    private Cell[][] mineField;
    private int numberCleared;
    private boolean shouldReset;

    public MineField() {
        numberOfMines = 10;
        numberOfRows = 20;
        numberOfCols = 20;
        numberCleared = 0;
        shouldReset = false;
        createMineField();
    }
    
    public MineField(int rows, int cols, int mines) {
        numberOfRows = rows;
        numberOfCols = cols;
        numberOfMines = mines;
        shouldReset = false;
        createMineField();
    }
    
    public void setShouldReset(boolean b) {
        shouldReset = b;
    }
    
    public boolean shouldReset() {
        return shouldReset;
    }

    /**
     * Increases the number of clicked cells by 1.
     */
    public void increaseNumberCleared() {
        numberCleared++;
    }

    /**
     * The number of cells which have been clicked and which did not turn out to
     * be mines.
     *
     * @return
     */
    public int getNumberCleared() {
        return numberCleared;
    }

    /**
     * The total number of cells on the board, including those which are mines.
     * @return 
     */
    public int getNumberOfCells() {
        return numberOfRows * numberOfCols;
    }

    /**
     * Creates a new minefield where mines are placed randomly across the field
     * based on the minePecentage variable.
     */
    public void createMineField() {
        mineField = new Cell[numberOfRows][numberOfCols];

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                mineField[row][col] = new Cell(row, col);
            }
        }

        Random r = new Random(); // randomly place mines
        for (int i = 0; i < getNumberOfMines(); i++) {
            int row = r.nextInt(numberOfRows);
            int col = r.nextInt(numberOfCols);
            if(mineField[row][col].getState() != 1) { // make sure mine hasen't already been placed here.
                mineField[row][col].setState(1);
            } else { // there is already a mine here. Reduce counter so we can try again.
                i--;
            }
        }

        countNeighbouringMines();
    }

    /**
     * Iterate through the minefield and count how many mines border to each
     * cell.
     */
    private void countNeighbouringMines() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                if (mineField[row][col].getState() == 1) {

                    if (row - 1 >= 0) {
                        mineField[row - 1][col].increaseNeighbourCount();
                        if (col + 1 < numberOfCols) {
                            mineField[row - 1][col + 1].increaseNeighbourCount();
                        }
                        if (col - 1 >= 0) {
                            mineField[row - 1][col - 1].increaseNeighbourCount();

                        }

                    }

                    if (row + 1 < numberOfRows) {
                        if (col + 1 < numberOfCols) {
                            mineField[row + 1][col + 1].increaseNeighbourCount();
                        }
                        if (col - 1 >= 0) {
                            mineField[row + 1][col - 1].increaseNeighbourCount();
                        }
                        mineField[row + 1][col].increaseNeighbourCount();
                    }

                    if (col + 1 < numberOfCols) {
                        mineField[row][col + 1].increaseNeighbourCount();
                    }

                    if (col - 1 >= 0) {
                        mineField[row][col - 1].increaseNeighbourCount();
                    }

                }
            }
        }
    }

    public Cell[][] getMineFieldArray() {
        return mineField;
    }

    /**
     * The number of mines in the minefield.
     *
     * @return an int specifying the number of mines in the minefield.
     */
    public int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Set the percentage (as a whole number) of the minefields cells which are
     * to be mines. If the minefield has 100 cells and this method recieves 10
     * as a parameter, 10 of the cells will be mines. Changes won't be applied
     * until a new game starts.
     *
     * @param percentage an int specifying the number of mines in percent.
     */
    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }

    /**
     * Set the number of rows for the minefield.
     *
     * @param newHeight an int specifying the number of rows.
     */
    public void setRows(int newHeight) {
        numberOfRows = newHeight;
    }

    /**
     * Gets the number of rows in the minefield.
     *
     * @return an int specifying the number of rows.
     */
    public int getRows() {
        return numberOfRows;
    }

    /**
     * Sets the number of columns in the minefield.
     *
     * @param newWidth an int specifying the number of columns.
     */
    public void setColumns(int newWidth) {
        numberOfCols = newWidth;
    }

    /**
     * Gets the number of columns in the minefield.
     *
     * @return an int specifying the number of columns.
     */
    public int getColumns() {
        return numberOfCols;
    }
}
