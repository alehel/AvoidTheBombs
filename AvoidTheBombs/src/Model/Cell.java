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

/**
 *
 * @author aleks
 */
public class Cell {

    private int state; // 1 means mine, 0 means no mine
    private int neighbourCount;
    private boolean hasBeenClicked;
    private boolean flagged;
    private boolean questioned;
    private int rowPosition;
    private int colPosition;

    public Cell(int row, int col) {
        state = 0;
        hasBeenClicked = false;
        flagged = false;
        questioned = false;
        rowPosition = row;
        colPosition = col;
    }

    public int getRow() {
        return rowPosition;
    }

    public int getCol() {
        return colPosition;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isQuestioned() {
        return questioned;
    }

    /**
     * Toggles the protected state of a cell. If a cell hasn't been clicked, it
     * becomes flagged. A flagged cell becomes a question mark. A questioned
     * cell becomes a normal non-clicked cell again.
     */
    public void toggleProtected() {
        if (!flagged && !questioned) { // currently regular state
            flagged = true;
        } else if (flagged) { // currently flagged
            flagged = false;
            questioned = true;
        } else { // currently questioned
            questioned = false;
        }
    }

    public void setClicked() {
        hasBeenClicked = true;
    }

    /**
     * Returns true if this cell has already been left-clicked during the game.
     *
     * @return
     */
    public boolean alreadyClicked() {
        return hasBeenClicked;
    }

    /**
     * Increases the neighbour count by 1.
     */
    public void increaseNeighbourCount() {
        neighbourCount++;
    }

    /**
     * Returns the number of mines this cell has in neighbouring cells.
     *
     * @return
     */
    public int getNeighbourCount() {
        return neighbourCount;
    }

    /**
     * Gets the state of the cell where 1 means its a mine, 0 means its not a
     * mine.
     *
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state of the cell. 1 means its a mine, 0 means its not a mine.
     * @param state 
     */
    public void setState(int state) {
        this.state = state;
    }
}
