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
import model.Ingredient;
import model.Inventory;
import model.NonPerishable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyIngredientController implements Initializable {
    @FXML
    public Label modifyIngredientLabel;
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

    private static int idInput;
    private static String ingredientNameInput;
    private static String initialNameInput;
    private static int stockInput;
    private static double priceEachInput;
    private static int servingsNumberInput;
    private static String unitTypeInput;
    private static String switchBoxInput;
    private static boolean isNonPerishable;

    public static void setInitialModifyIngredient(Ingredient selectedIngredient) {

        idInput = selectedIngredient.getId();
        ingredientNameInput = selectedIngredient.getIngredientName();
        initialNameInput = selectedIngredient.getIngredientName();
        stockInput = selectedIngredient.getStock();
        priceEachInput = selectedIngredient.getPricePerEach();
        servingsNumberInput = selectedIngredient.getServingsPerContainer();
        unitTypeInput = selectedIngredient.getUnitOfMeasure();
        if(selectedIngredient instanceof NonPerishable){
            switchBoxInput = ((NonPerishable) selectedIngredient).getLongDate();
            isNonPerishable = true;
        }
        else{
            switchBoxInput = String.valueOf(((Perishable) selectedIngredient).getShortDate());
            isNonPerishable = false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyIngredientLabel.setText("Modify Ingredient");
        modifyIngredientLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 18));

        ingredientNameBox.setText("" + ingredientNameInput);
        pricePerEachBox.setText("" + priceEachInput);
        stockBox.setText("" + stockInput);
        unitOfMeasureBox.setText("" + unitTypeInput);
        servingsPerContainerBox.setText("" + servingsNumberInput);
        switchBox.setText("" + switchBoxInput);
        if (isNonPerishable) {
            nonPerishableRadio.setSelected(true);
            toggleNonPerishable(new ActionEvent());
        }
        else {
            perishableRadio.setSelected(true);
            togglePerishable(new ActionEvent());
        }
    }

    public void togglePerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Short Expiration Date \n(MM/DD/YYYY)");
    }

    public void toggleNonPerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Long Expiration Date \n(MM/YYYY)");
    }

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

        String nameInput = ingredientNameBox.getText();
        if(nameInput.isBlank()){
            Alert nameBlankError = new Alert(Alert.AlertType.ERROR);
            nameBlankError.setTitle("Ingredient Name Field Is Empty");
            nameBlankError.setHeaderText("Ingredient name field must have a string value");
            nameBlankError.setContentText("Please enter a name using letters/numbers/spaces only");
            nameBlankError.showAndWait();
            return;
        }

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

        int stockInput = Integer.parseInt(stockBox.getText());
        double priceEachInput = Double.parseDouble(pricePerEachBox.getText());
        String unitTypeInput = unitOfMeasureBox.getText();
        int servingsNumberInput = Integer.parseInt(servingsPerContainerBox.getText());

        if (perishableRadio.isSelected()) {
            String shortDateInput = switchBox.getText();

            if(shortDateInput.isBlank()){
                Alert shortDateEmptyAlert = new Alert(Alert.AlertType.ERROR);
                shortDateEmptyAlert.setTitle("Short Date Field Is Empty");
                shortDateEmptyAlert.setHeaderText("Short date field must have a value of MM/YYYY");
                shortDateEmptyAlert.setContentText("Please enter a valid date with month and year format.");
                shortDateEmptyAlert.showAndWait();
                return;
            }
            Perishable perishIngredient = new Perishable(idInput, nameInput, priceEachInput,
                    stockInput, unitTypeInput, servingsNumberInput, shortDateInput);
            Inventory.updateIngredient(initialNameInput, perishIngredient);
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
                longDateEmptyAlert.setHeaderText("Long date field must have a value of MM/DD/YYYY");
                longDateEmptyAlert.setContentText("Please enter a valid date with month, day, year format.");
                longDateEmptyAlert.showAndWait();
                return;
            }

            NonPerishable nonPerishIngredient = new NonPerishable(idInput, nameInput, priceEachInput,
                    stockInput, unitTypeInput, servingsNumberInput, longDateInput);
            Inventory.updateIngredient(initialNameInput, nonPerishIngredient);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("To Taste: Main Screen");
            stage.setScene(scene);
            stage.show();
        }
    }
}
