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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddRecipeController implements Initializable {
    public Label addRecipeLabel;
    public TextField recipeNameBox;
    public TextField recipeServingsBox;
    public TextField recipeCostBox;
    public TextArea flavorTagsBox;
    public TableView<Ingredient> allIngredientsTable;
    public TableColumn<Ingredient,?> ingredientName;
    public TableColumn<Ingredient,?> ingredientPricePerEach;
    public TextField queryIngredients;
    public TableView<Ingredient> requiredIngredientsTable;
    public TableColumn<Ingredient, ?> requiredIngredientName;
    public TableColumn<Ingredient, ?> requiredIngredientNumberOfUnits;
    public TableColumn<Ingredient, ?> requiredIngredientPricePerEach;
    public Label errorBox;

    private final ObservableList<Ingredient> tempRequiredIngredients = FXCollections.observableArrayList();
    private static final ObservableList<Ingredient> displayRequiredIngredients = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addRecipeLabel.setText("Add Recipe");
        addRecipeLabel.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 18));

        allIngredientsTable.setItems(Inventory.getAllIngredients());
        ingredientName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        ingredientPricePerEach.setCellValueFactory(new PropertyValueFactory<>("concatPriceUnit"));

        requiredIngredientName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        requiredIngredientNumberOfUnits.setCellValueFactory(new PropertyValueFactory<>("numberOfUnits"));
        requiredIngredientPricePerEach.setCellValueFactory(new PropertyValueFactory<>("concatPriceUnit"));

        recipeCostBox.setText("$0.00");
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 466,665);
        stage.setTitle("To Taste: Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void onAddIngredientClick() {
        Ingredient selectedIngredient = allIngredientsTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        tempRequiredIngredients.add(selectedIngredient);
        confirmIdAndCount();
        requiredIngredientsTable.setItems(displayRequiredIngredients);
        double tempRecipeCost = Double.parseDouble(recipeCostBox.getText(1, 5)) +
                (selectedIngredient.getPricePerContainer() / selectedIngredient.getUnitsPerContainer());
        recipeCostBox.setText("$" + String.format("%.2f", tempRecipeCost));
    }

    public void onRemoveRequiredIngredientClick() {
        Ingredient selectedIngredient = requiredIngredientsTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        Alert sureDeleteRequiredIngredient = new Alert(Alert.AlertType.CONFIRMATION);
        sureDeleteRequiredIngredient.setTitle("Required Ingredients");
        sureDeleteRequiredIngredient.setHeaderText("Delete Required Ingredient " +
                selectedIngredient.getIngredientName() + "?");
        sureDeleteRequiredIngredient.setContentText("Do you want to delete this ingredient?");
        Optional<ButtonType> confirm = sureDeleteRequiredIngredient.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        tempRequiredIngredients.remove(selectedIngredient);
        confirmIdAndCount();
        double tempRecipeCost = Double.parseDouble(recipeCostBox.getText(1, 5)) -
                (selectedIngredient.getPricePerContainer() / selectedIngredient.getUnitsPerContainer());
        recipeCostBox.setText("$" + String.format("%.2f", tempRecipeCost));
    }

    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        String errorCollector = "";

        try {
            int invTestInput = Integer.parseInt(recipeServingsBox.getText());
        } catch (NumberFormatException e) {
            errorCollector += "--Servings input must be an integer greater than 0 \n";
        }

        if (!errorCollector.isBlank()){
            Alert exceptionErrors = new Alert(Alert.AlertType.ERROR);
            exceptionErrors.setTitle("Errors Causing Number Format Exception");
            exceptionErrors.setHeaderText("Exception caused by:");
            exceptionErrors.setContentText(errorCollector);
            exceptionErrors.showAndWait();
            return;
        }

        String nameInput = recipeNameBox.getText();
        if(nameInput.isBlank()){
            Alert nameBlankError = new Alert(Alert.AlertType.ERROR);
            nameBlankError.setTitle("Name Field Is Empty");
            nameBlankError.setHeaderText("Name field must have a string value");
            nameBlankError.setContentText("Please enter a name using letters/numbers/spaces only");
            nameBlankError.showAndWait();
            return;
        }
        int invInput = Integer.parseInt(recipeServingsBox.getText());
        double priceCostInput = Double.parseDouble(recipeCostBox.getText(1,4));
        String flavorTagInput = flavorTagsBox.getText();

        Recipe recipe = new Recipe(nameInput, priceCostInput,
                invInput, flavorTagInput);

        for (Ingredient tempRequiredIngredient : tempRequiredIngredients) {
            recipe.addRequiredIngredient(tempRequiredIngredient);
        }

        Inventory.addRecipe(recipe);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 466,665);
        stage.setTitle("To Taste: Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void ingredientSearchActivate() {
        errorBox.setText("");
        String searchString = queryIngredients.getText();

        ObservableList<Ingredient> ingredients = Inventory.lookupIngredient(searchString);
        if (ingredients.size() == 0){
            errorBox.setText("Ingredient not found");
        }
        allIngredientsTable.setItems(ingredients);
    }
    public void confirmIdAndCount(){
        int[] recipeIngredientCount = new int[Inventory.getAllIngredients().size()];

        while(displayRequiredIngredients.size() > 0){
            displayRequiredIngredients.remove(0);
        }

        if (tempRequiredIngredients.size() == 1){
            Ingredient onlyOneIngredient = tempRequiredIngredients.get(0);
            onlyOneIngredient.setNumberOfUnits(1);
            displayRequiredIngredients.add(onlyOneIngredient);
            requiredIngredientsTable.refresh();
            return;
        }

        for(int i = 1; i < tempRequiredIngredients.size(); ++i){
            boolean alreadyDidThisId = false;
            int indexPosition = 0;
            Ingredient thisIngredient = tempRequiredIngredients.get(i);
            for (int j = (i - 1); j >= 0; --j) {
                Ingredient prevIngredient = tempRequiredIngredients.get(j);
                if (i == 1) {
                    recipeIngredientCount[indexPosition] = countTempIngredients(prevIngredient.getId());
                    prevIngredient.setNumberOfUnits(recipeIngredientCount[indexPosition]);
                    displayRequiredIngredients.add(prevIngredient);
                    ++indexPosition;
                }
                if (thisIngredient.getId() == prevIngredient.getId()) {
                    alreadyDidThisId = true;
                    break;
                }
            }
            if(!alreadyDidThisId){
                recipeIngredientCount[indexPosition] = countTempIngredients(thisIngredient.getId());
                thisIngredient.setNumberOfUnits(recipeIngredientCount[indexPosition]);
                displayRequiredIngredients.add(thisIngredient);
                ++indexPosition;
            }
        }
        requiredIngredientsTable.refresh();
    }

    public int countTempIngredients(int tempIngredientId){
        int countId = 0;
        for (Ingredient tempRequiredIngredient : tempRequiredIngredients) {
            if (tempIngredientId == tempRequiredIngredient.getId()) {
                countId += 1;
            }
        }
        return countId;
    }
}