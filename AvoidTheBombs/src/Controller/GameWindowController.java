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
package Controller;

import Model.Cell;
import Model.MineField;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author aleks
 */
public class GameWindowController implements Initializable {

    private MineField mineField;
    private Button[][] buttonArray;
    private String bomb = "💣";
    private String flag = "⚑";
    private String question = "?";
    private boolean gameOver;
    private boolean win;
    private Cell[][] boardArray;

    @FXML
    private Label label;

    @FXML
    private GridPane mineFieldGrid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newGame();
    }

    /**
     * Start a new game using default values for size and number of mines.
     */
    @FXML
    private void newGame() {
        mineField = new MineField();
        newGame(mineField);
    }

    /**
     * Start a new game using an existing MineField object.
     *
     * @param mineField
     */
    private void newGame(MineField mineField) {
        this.mineField = mineField;
        buttonArray = new Button[mineField.getRows()][mineField.getColumns()];
        gameOver = false;
        win = false;
        generateBoard();
    }

    /**
     * Generates the GridPane of Buttons and attach their event handler.
     */
    private void generateBoard() {
        mineFieldGrid.getChildren().clear(); // clear the GridPane so old buttons don't remain.
        boardArray = mineField.getMineFieldArray();

        for (int row = 0; row < mineField.getRows(); row++) {
            for (int col = 0; col < mineField.getColumns(); col++) {
                Button button = new Button();
                button.setMinSize(30, 30);
                Cell cell = boardArray[row][col];

                button.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    mouseClickOnCell(cell, button, event);
                });
                buttonArray[row][col] = button;
                mineFieldGrid.setRowIndex(button, row);
                mineFieldGrid.setColumnIndex(button, col);
                mineFieldGrid.getChildren().addAll(button);
            }
        }
    }

    /**
     * Handle the mouse click events on cell buttons.
     *
     * @param cell the Cell object which has been clicked.
     * @param button the Button which was clicked.
     */
    private void mouseClickOnCell(Cell cell, Button button, MouseEvent event) {
        if (!gameOver && !win) { // ensure the game is still on
            if (event.getButton() == MouseButton.PRIMARY) { // left click
                triggerCell(cell, button);
            } else if (event.getButton() == MouseButton.SECONDARY) { // right click
                toggleProtectedCellState(cell, button);
            }
        } else if (gameOver) {
            showYouDiedAlert();
        }

        // check to see if player won.
        if (mineField.getNumberCleared() == (mineField.getNumberOfCells() - mineField.getNumberOfMines())) {
            win = true;
            showYouWinAlert();
        }
    }

    /**
     * Handle left-click events
     *
     * @param cell the cell which was clicked.
     * @param button the button which represented the cell on the board.
     */
    private void triggerCell(Cell cell, Button button) {
        if (!cell.alreadyClicked() && !cell.isFlagged() && !cell.isQuestioned()) { // ensure this is first left-click
            cell.setClicked();
            if (cell.getState() == 1) { // cell is a mine
                showAllMines();
                gameOver = true;
                showYouDiedAlert();
            } else if (cell.getNeighbourCount() != 0) { // cell is not a mine but does neighbour to one
                button.setText(String.valueOf(cell.getNeighbourCount()));
                button.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000");
                mineField.increaseNumberCleared();
            } else { // cell is not a mine and also doesn't neighbour to any.
                emptyCell(cell);
                button.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000");
            }
        }
    }

    /**
     * An empty cell with no neighbouring mines has been clicked. Recursion is
     * used to reveal all neighbouring cells which also have no neighbouring
     * mines.
     */
    private void emptyCell(Cell cell) {
        mineField.increaseNumberCleared();
        int row = cell.getRow();
        int col = cell.getCol();

        if (cell.getState() == 0 && cell.getNeighbourCount() == 0) {
            cell.setClicked();
            buttonArray[row][col].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000");
            Cell tmpCell;

            if (row - 1 >= 0) {
                tmpCell = boardArray[row - 1][col];
                if (tmpCell.getState() == 0 && tmpCell.getNeighbourCount() == 0 && !tmpCell.alreadyClicked()) {
                    emptyCell(tmpCell);
                }
            }

            if (row + 1 < mineField.getRows()) {
                tmpCell = boardArray[row + 1][col];
                if (tmpCell.getState() == 0 && tmpCell.getNeighbourCount() == 0 && !tmpCell.alreadyClicked()) {
                    emptyCell(tmpCell);
                }
            }

            if (col + 1 < mineField.getColumns()) {
                tmpCell = boardArray[row][col + 1];
                if (tmpCell.getState() == 0 && tmpCell.getNeighbourCount() == 0 && !tmpCell.alreadyClicked()) {
                    emptyCell(tmpCell);
                }
            }

            if (col - 1 >= 0) {
                tmpCell = boardArray[row][col - 1];
                if (tmpCell.getState() == 0 && tmpCell.getNeighbourCount() == 0 && !tmpCell.alreadyClicked()) {
                    emptyCell(tmpCell);
                }
            }
        }

    }

    /**
     * Toggles the protected state of a cell. If a cell hasn't been clicked it,
     * it becomes flagged. A flagged cell becomes a question mark. A questioned
     * cell becomes a normal non-clicked cell again.
     *
     * @param cell the cell which has been clicked.
     * @param button the button which represents the clicked cell.
     */
    private void toggleProtectedCellState(Cell cell, Button button) {
        if (!cell.alreadyClicked()) {
            cell.toggleProtected();
            if (cell.isFlagged()) {
                button.setText(flag);
            } else if (cell.isQuestioned()) {
                button.setText("?");
            } else {
                button.setText("");
            }
        }
    }

    /**
     * When player dies, we want to show the location of all mines.
     */
    private void showAllMines() {
        Cell[][] mines = mineField.getMineFieldArray();
        for (int row = 0; row < mines.length; row++) {
            for (int col = 0; col < mines.length; col++) {
                if (mines[row][col].getState() == 1) {
                    buttonArray[row][col].setText(bomb);
                    buttonArray[row][col].setStyle("-fx-background-color: #ee5353");
                }
            }
        }
    }

    /**
     * Show a dialogue informing the player that they have died. Offer them to
     * start a new game.
     */
    private void showYouDiedAlert() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Game Over");
        alert.setContentText("Sorry, you hit a bomb. Would you like to try again?!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mineField = new MineField(mineField.getRows(),
                    mineField.getColumns(), mineField.getNumberOfMines());
            newGame(mineField);
        }
    }

    /**
     * Inform the player that they have won. Offer them to start a new game.
     */
    private void showYouWinAlert() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(bomb);
        alert.setContentText("Congratulations, you win! Play again?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mineField = new MineField(mineField.getRows(),
                    mineField.getColumns(), mineField.getNumberOfMines());
            newGame(mineField);
        }
    }

    @FXML
    private void showSettingsWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/SettingsWindow.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.getIcons().add((new Image("/Assets/bomb.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");
            stage.setScene(new Scene(root));
            SettingsWindowController controller = fxmlLoader.getController();
            controller.setMineField(mineField);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mineField.shouldReset()) {
            mineField = new MineField(mineField.getRows(),
                    mineField.getColumns(), mineField.getNumberOfMines());
            newGame(mineField);
        }

    }

}
