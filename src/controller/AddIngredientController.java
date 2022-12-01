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
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public Label addPartLabel;
    public Label toggleTextSwitch;
    public TextField nameBox;
    public TextField invBox;
    public TextField priceCostBox;
    public TextField maxBox;
    public TextField switchBox;
    public TextField minBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPartLabel.setText("Add Part");
        addPartLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 18));
        togglePerishable(new ActionEvent());
    }

    public void toggleNonPerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Company Name");
    }

    public void togglePerishable(ActionEvent actionEvent) {
        toggleTextSwitch.setText("Machine ID");
    }

    @FXML
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

        String nameInput = nameBox.getText();
        if(nameInput.isBlank()){
            Alert nameBlankError = new Alert(Alert.AlertType.ERROR);
            nameBlankError.setTitle("Name Field Is Empty");
            nameBlankError.setHeaderText("Name field must have a string value");
            nameBlankError.setContentText("Please enter a name using letters/numbers/spaces only");
            nameBlankError.showAndWait();
            return;
        }

        try {
            int invTestInput = Integer.parseInt(invBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Inv input must be an integer between Min and Max values \n";
        }
        try {
            double priceCostTestInput = Double.parseDouble(priceCostBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Price/cost input must be a floating point number \n";
        }
        try {
            int maxTestInput = Integer.parseInt(maxBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Max input must be an integer greater than or equal to Min input \n";
        }
        try {
            int minTestInput = Integer.parseInt(minBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Min input must be an integer less than or equal to Max input \n";
        }
        if (inHouseRadio.isSelected()) {
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

        //int idInput = Inventory.findNewIngredientId();
        int invInput = Integer.parseInt(invBox.getText());
        double priceCostInput = Double.parseDouble(priceCostBox.getText());
        int maxInput = Integer.parseInt(maxBox.getText());
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

        if (inHouseRadio.isSelected()) {
            int machineIdInput = Integer.parseInt(switchBox.getText());

            Perishable part = new Perishable(nameInput, priceCostInput,
                    invInput, minInput, maxInput, machineIdInput);
            Inventory.addIngredient(part);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }

        else if(outsourcedRadio.isSelected()) {
            String companyNameInput = switchBox.getText();
            if(companyNameInput.isBlank()){
                Alert companyNameBlankError = new Alert(Alert.AlertType.ERROR);
                companyNameBlankError.setTitle("Company Name Field Is Empty");
                companyNameBlankError.setHeaderText("Company Name field must have a string value");
                companyNameBlankError.setContentText("Please enter a Company Name using letters/numbers/spaces only");
                companyNameBlankError.showAndWait();
                return;
            }

            NonPerishable part = new NonPerishable(nameInput, priceCostInput,
                    invInput, minInput, maxInput, companyNameInput);
            Inventory.addIngredient(part);
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }
}
