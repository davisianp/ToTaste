package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Perishable;
import model.Inventory;
import model.NonPerishable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddIngredientController implements Initializable {
    public Label addIngredientLabel;
    public RadioButton perishableRadio;
    public RadioButton nonPerishableRadio;
    public TextField ingredientNameBox;
    public TextField stockBox;
    public TextField pricePerEachBox;
    public TextField unitOfMeasureBox;
    public TextField servingsPerContainerBox;
    public TextField switchBox;
    public Label toggleTextSwitch;
    public ToggleGroup perishableStateGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addIngredientLabel.setText("Add Ingredient");
        addIngredientLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 18));
        togglePerishable(new ActionEvent());
    }

    public void toggleNonPerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Long Expiration Date \n(MM/YYYY)");
    }

    public void togglePerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Short Expiration Date \n(MM/DD/YYYY)");
    }

    @FXML
    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("To Taste: Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        String errorCollector = "";

        try {
            double priceCostTestInput = Double.parseDouble(pricePerEachBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Price/cost input must be a floating point number \n";
        }
        try {
            int servingsTestInput = Integer.parseInt(servingsPerContainerBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--number of servings in a container must be an integer \n";
        }

        if (!errorCollector.isBlank()){
            Alert exceptionErrors = new Alert(Alert.AlertType.ERROR);
            exceptionErrors.setTitle("Errors Causing Number Format Exception");
            exceptionErrors.setHeaderText("Exception caused by:");
            exceptionErrors.setContentText(errorCollector);
            exceptionErrors.showAndWait();
            return;
        }

        String nameInput = ingredientNameBox.getText();
        if(nameInput.isBlank()){
            Alert nameBlankError = new Alert(Alert.AlertType.ERROR);
            nameBlankError.setTitle("Ingredient Name Field Is Empty");
            nameBlankError.setHeaderText("Ingredient name field must have a string value");
            nameBlankError.setContentText("Please enter a name using letters/numbers/spaces only");
            nameBlankError.showAndWait();
            return;
        }

        String unitTypeInput = unitOfMeasureBox.getText();
        if(unitTypeInput.isBlank()){
            Alert unitTypeBlankError = new Alert(Alert.AlertType.ERROR);
            unitTypeBlankError.setTitle("Unit Of Measurement Field Is Empty");
            unitTypeBlankError.setHeaderText("Unit of measurement field must have a string value");
            unitTypeBlankError.setContentText("Please enter a type of measurement used for this ingredient using letters/numbers/spaces only");
            unitTypeBlankError.showAndWait();
            return;
        }

        int idInput = Inventory.pickNewId();
        int stockInput = Integer.parseInt(stockBox.getText());
        double priceEachInput = Double.parseDouble(pricePerEachBox.getText());
        int servingsNumberInput = Integer.parseInt(servingsPerContainerBox.getText());

        if (perishableRadio.isSelected()) {
            String shortDateInput = switchBox.getText();

            if(shortDateInput.isBlank()){
                Alert shortDateEmptyAlert = new Alert(Alert.AlertType.ERROR);
                shortDateEmptyAlert.setTitle("Short Date Field Is Empty");
                shortDateEmptyAlert.setHeaderText("Short date field must have a value of MM/DD/YYYY");
                shortDateEmptyAlert.setContentText("Please enter a valid date with month, day, and year format.");
                shortDateEmptyAlert.showAndWait();
                return;
            }
            if(!shortDateInput.matches("(?:0[1-9]|1[012])[-/.](?:0[1-9]|[12][0-9]|3[01])[-/.](?:20\\d{2}|20[01][0-9]|2100)")){
                Alert shortDateIncorrectAlert = new Alert(Alert.AlertType.ERROR);
                shortDateIncorrectAlert.setTitle("Short Date Input Is Incorrect");
                shortDateIncorrectAlert.setHeaderText("Short date field must have a value of MM/DD/YYYY");
                shortDateIncorrectAlert.setContentText("Please enter a valid date with month, day, and year format.");
                shortDateIncorrectAlert.showAndWait();
                return;
            }
            Perishable ingredient = new Perishable(idInput, nameInput, priceEachInput,
                    stockInput, unitTypeInput, servingsNumberInput, shortDateInput);
            Inventory.addIngredient(ingredient);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("To Taste: Main Screen");
            stage.setScene(scene);
            stage.show();
        }

        else if(nonPerishableRadio.isSelected()) {
            String longDateInput = switchBox.getText();
            if(longDateInput.isBlank()){
                Alert longDateEmptyAlert = new Alert(Alert.AlertType.ERROR);
                longDateEmptyAlert.setTitle("Long Date Field Is Empty");
                longDateEmptyAlert.setHeaderText("Long date field must have a value of MM/YYYY");
                longDateEmptyAlert.setContentText("Please enter a valid date with month and year format.");
                longDateEmptyAlert.showAndWait();
                return;
            }
            if(!longDateInput.matches("(?:0[1-9]|1[012])[-/.](?:20\\d{2}|20[01][0-9]|2100)")){
                Alert longDateIncorrectAlert = new Alert(Alert.AlertType.ERROR);
                longDateIncorrectAlert.setTitle("Long Date Input Is Incorrect");
                longDateIncorrectAlert.setHeaderText("Long date field must have a value of MM/YYYY");
                longDateIncorrectAlert.setContentText("Please enter a valid date with month and year format.");
                longDateIncorrectAlert.showAndWait();
                return;
            }

            NonPerishable ingredient = new NonPerishable(idInput, nameInput, priceEachInput,
                    stockInput, unitTypeInput, servingsNumberInput, longDateInput);
            Inventory.addIngredient(ingredient);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("To Taste: Main Screen");
            stage.setScene(scene);
            stage.show();
        }
    }
}
