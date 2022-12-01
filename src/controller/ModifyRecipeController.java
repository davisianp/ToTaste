package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Ingredient;
import model.Inventory;
import model.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyRecipeController implements Initializable {
    public Label modifyProductLabel;
    public TextField idBox;
    public TextField nameBox;
    public TextField invBox;
    public TextField priceBox;
    public TextField maxBox;
    public TextField minBox;
    public TableView<Ingredient> allPartsTable;
    public TableColumn<Ingredient, ?> partId;
    public TableColumn<Ingredient, ?> partName;
    public TableColumn<Ingredient, ?> partInventoryLevel;
    public TableColumn<Ingredient, ?> partPriceCostPerItem;
    public TableView<Ingredient> associatedPartsTable;
    public TextField queryParts;
    public TableColumn<Ingredient, ?> associatedPartId;
    public TableColumn<Ingredient, ?> associatedPartName;
    public TableColumn<Ingredient, ?> associatedPartInvLevel;
    public TableColumn<Ingredient, ?> associatedPricePerItem;

    private static int idInput;
    private static String nameInput;
    private static int invInput;
    private static double priceCostInput;
    private static int maxInput;
    private static int minInput;
    private static ObservableList<Ingredient> tempRequiredIngredients = FXCollections.observableArrayList();
    public Label errorBox;

    public static void setInitialModifyRecipe(Recipe selectedRecipe) {

        idInput = selectedRecipe.getId();
        nameInput = selectedRecipe.getName();
        invInput = selectedRecipe.getStock();
        priceCostInput = selectedRecipe.getPrice();
        maxInput = selectedRecipe.getMax();
        minInput = selectedRecipe.getMin();
        tempRequiredIngredients = selectedRecipe.getAllRequiredIngredients();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyProductLabel.setText("Modify Product");
        modifyProductLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 18));

        allPartsTable.setItems(Inventory.getAllIngredients());
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostPerItem.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(tempRequiredIngredients);
        associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPricePerItem.setCellValueFactory(new PropertyValueFactory<>("price"));

        idBox.setText("" + idInput);
        nameBox.setText("" + nameInput);
        priceBox.setText("" + priceCostInput);
        invBox.setText("" + invInput);
        maxBox.setText("" + maxInput);
        minBox.setText("" + minInput);

        associatedPartsTable.getSortOrder().add(associatedPartId);

    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void onAddIngredientClick(ActionEvent actionEvent) {
        Ingredient selectedIngredient = allPartsTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        tempRequiredIngredients.add(selectedIngredient);
        associatedPartsTable.setItems(tempRequiredIngredients);
    }

    public void onRemoveRequiredIngredientClick(ActionEvent actionEvent) {
        Ingredient selectedIngredient = associatedPartsTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        Alert sureDeleteAssociatedPart = new Alert(Alert.AlertType.CONFIRMATION);
        sureDeleteAssociatedPart.setTitle("Associated Parts");
        sureDeleteAssociatedPart.setHeaderText("Delete Associated Part ID " +
                selectedIngredient.getName() + "?");
        sureDeleteAssociatedPart.setContentText("Do you want to delete this part?");
        Optional<ButtonType> confirm = sureDeleteAssociatedPart.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        tempRequiredIngredients.remove(selectedIngredient);
        associatedPartsTable.setItems(tempRequiredIngredients);
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
            double priceCostTestInput = Double.parseDouble(priceBox.getText());
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

        if (!errorCollector.isBlank()){
            Alert exceptionErrors = new Alert(Alert.AlertType.ERROR);
            exceptionErrors.setTitle("Errors Causing Number Format Exception");
            exceptionErrors.setHeaderText("Exception caused by:");
            exceptionErrors.setContentText(errorCollector);
            exceptionErrors.showAndWait();
            return;
        }

        int idInput = Integer.parseInt(idBox.getText());
        int invInput = Integer.parseInt(invBox.getText());
        double priceCostInput = Double.parseDouble(priceBox.getText());
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

        Recipe recipe = new Recipe(idInput, nameInput, priceCostInput,
                invInput, minInput, maxInput);

        for (Ingredient tempRequiredIngredient : tempRequiredIngredients) {
            recipe.addRequiredIngredient(tempRequiredIngredient);
        }

        Inventory.updateRecipe(idInput, recipe);
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void ingredientSearchActivate() {
        errorBox.setText("");
        String searchString = queryParts.getText();

        ObservableList<Ingredient> ingredients = Inventory.lookupIngredient(searchString);
        if (ingredients.size() == 0){
            /*try {
                int searchInt = Integer.parseInt(searchString);
                Ingredient searchIngredient = Inventory.lookupIngredient(searchInt);
                if (searchIngredient != null) {
                    ingredients.add(searchIngredient);
                }
                else {
                    errorBox.setText("Cannot find part ID number");
                }
            }
            catch(NumberFormatException e) {*/
                errorBox.setText("Part not found");
            //}
        }
        allPartsTable.setItems(ingredients);
    }
}
