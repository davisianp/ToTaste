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
    public TextField maxEachesBox;
    public TextField minBox; // FIX ME: split this box into two data members
    public TextField switchBox;
    public Label toggleTextSwitch;

    private static String ingredientNameInput;
    private static int stockInput;
    private static double priceEachInput;
    private static int maxEachesInput;
    private static int minInput; // FIX ME
    private static String switchBoxInput;
    private static boolean isNonPerishable;
    public ToggleGroup perishableStateGroup;

    public static void setInitialModifyIngredient(Ingredient selectedIngredient) {

        ingredientNameInput = selectedIngredient.getName();
        stockInput = selectedIngredient.getStock();
        priceEachInput = selectedIngredient.getPrice();
        maxEachesInput = selectedIngredient.getMax();
        minInput = selectedIngredient.getMin();
        if(selectedIngredient instanceof NonPerishable){
            switchBoxInput = ((NonPerishable) selectedIngredient).getCompanyName();
            isNonPerishable = true;
        }
        else{
            switchBoxInput = String.valueOf(((Perishable) selectedIngredient).getMachineId());
            isNonPerishable = false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyIngredientLabel.setText("Modify Part");
        modifyIngredientLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 18));

        ingredientNameBox.setText("" + ingredientNameInput);
        pricePerEachBox.setText("" + priceEachInput);
        stockBox.setText("" + stockInput);
        maxEachesBox.setText("" + maxEachesInput);
        minBox.setText("" + minInput);
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
        toggleTextSwitch.setText("Expiration Date \n(MM/DD/YYYY)");
    }

    public void toggleNonPerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Expiration Date \n(MM/YYYY)");
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Inventory Management System");
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
            int maxTestInput = Integer.parseInt(maxEachesBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Max input must be an integer greater than or equal to Min input \n";
        }
        try {
            int minTestInput = Integer.parseInt(minBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Min input must be an integer less than or equal to Max input \n";
        }
        if (perishableRadio.isSelected()) {
            try {
                int machineIdTestInput = Integer.parseInt(switchBox.getText());
            } catch (NumberFormatException e) {
                errorCollector += "--Machine ID input must be an integer";
            }
        }

        if (!errorCollector.isBlank()){
            Alert exceptionErrors = new Alert(Alert.AlertType.ERROR);
            exceptionErrors.setTitle("Errors Causing Number Format Exception");
            exceptionErrors.setHeaderText("Exception caused by:");
            exceptionErrors.setContentText(errorCollector);
            exceptionErrors.showAndWait();
            return;
        }

        int invInput = Integer.parseInt(stockBox.getText());
        double priceCostInput = Double.parseDouble(pricePerEachBox.getText());
        int maxInput = Integer.parseInt(maxEachesBox.getText());
        int minInput = Integer.parseInt(minBox.getText());

        if (minInput > maxInput) {
            Alert minMaxError = new Alert(Alert.AlertType.ERROR);
            minMaxError.setTitle("Min/Max Unexpected Input");
            minMaxError.setHeaderText("Min was set as a higher value than Max");
            minMaxError.setContentText("Please enter Min as a lower value than Max");
            minMaxError.showAndWait();
            return;
        }
        if (minInput > invInput || invInput > maxInput) {
            Alert invBoundsError = new Alert(Alert.AlertType.ERROR);
            invBoundsError.setTitle("Inventory Unexpected Input");
            invBoundsError.setHeaderText("Inventory input is out of range given Min and Max");
            invBoundsError.setContentText("Please enter Inv as a number between (or equal to) Min and Max");
            invBoundsError.showAndWait();
            return;
        }

        if (perishableRadio.isSelected()) {
            int machineIdInput = Integer.parseInt(switchBox.getText());
            Perishable part = new Perishable( nameInput, priceCostInput,
                    invInput, minInput, maxInput, machineIdInput);
            Inventory.updateIngredient(nameInput, part);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
        else if(nonPerishableRadio.isSelected()) {
            String companyNameInput = switchBox.getText();
            if(companyNameInput.isBlank()){
                Alert companyNameBlankError = new Alert(Alert.AlertType.ERROR);
                companyNameBlankError.setTitle("Company Name Field Is Empty");
                companyNameBlankError.setHeaderText("Company Name field must have a string value");
                companyNameBlankError.setContentText("Please enter a Company Name using letters/numbers/spaces only");
                companyNameBlankError.showAndWait();
                return;
            }

            NonPerishable part = new NonPerishable( nameInput, priceCostInput,
                    invInput, minInput, maxInput, companyNameInput);
            Inventory.updateIngredient(nameInput, part);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }
}
