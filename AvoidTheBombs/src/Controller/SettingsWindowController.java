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

import Model.MineField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author aleks
 */
public class SettingsWindowController implements Initializable {

    MineField mineField;

    @FXML
    Button btnCancel;

    @FXML
    TextField txtRows, txtCols, txtMines;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(this::populateBoxes);
    }

    private void populateBoxes() {
        txtRows.setText(String.valueOf(mineField.getRows()));
        txtCols.setText(String.valueOf(mineField.getColumns()));
        txtMines.setText(String.valueOf(mineField.getNumberOfMines()));
    }

    @FXML
    private void clickedOK() {
        try {
            int rows = Integer.valueOf(txtRows.getText());
            int cols = Integer.valueOf(txtCols.getText());
            int mines = Integer.valueOf(txtMines.getText());

            if (rows < 0 || cols < 0) {
                throw new IllegalStateException("Number of rows and columns "
                        + "must be a non-negative value!");
            }

            if (mines < 0 || mines > (rows * cols)) {
                throw new IllegalStateException("Number of mines must be higher "
                        + "than 0 and lower than the total number of cells!");
            }

            mineField.setRows(rows);
            mineField.setColumns(cols);
            mineField.setNumberOfMines(mines);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Changing settings will restart the game. Are "
                    + "you sure you wish to continue.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                mineField.setShouldReset(true);
                close();
            }
        } catch (NumberFormatException e) {
            showErrorMessage("All values must be numbers!");
        } catch (IllegalStateException e) {
            showErrorMessage(e.getMessage());
        }

    }

    private void showErrorMessage(String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Invalid input");
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the MineField object.
     *
     * @param mineField
     */
    public void setMineField(MineField mineField) {
        this.mineField = mineField;
    }

}
