package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public Label ingredientInventory;
    public TableView<Recipe> inventoryRecipeTable;
    public TableView<Ingredient> inventoryIngredientTable;
    public Label recipeInventory;
    public TableColumn<Ingredient,?> ingredientName;
    public TableColumn<Ingredient,?> ingredientStock;
    public TableColumn<Ingredient,?> ingredientPricePerEach;
    public TableColumn<Recipe, ?> recipeName;
    public TableColumn<Recipe, ?> recipeServings;
    public TableColumn<Recipe, ?> recipeCost;
    public TextField queryIngredients;
    public TextField queryRecipes;
    public Label addRecipeLabel;
    public Label errorRecipeBox;
    public Label errorIngredientBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ingredientInventory.setText("Parts");
        ingredientInventory.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 12));
        recipeInventory.setText("Products");
        recipeInventory.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 12));

        inventoryIngredientTable.setItems(Inventory.getAllIngredients());
        inventoryRecipeTable.setItems(Inventory.getAllRecipes());

        ingredientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ingredientPricePerEach.setCellValueFactory(new PropertyValueFactory<>("price"));

        recipeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        recipeServings.setCellValueFactory(new PropertyValueFactory<>("stock"));
        recipeCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    @FXML
    public void onAddIngredientClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddIngredient.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add Ingredient Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyIngredientClick(ActionEvent actionEvent) throws IOException {
        Ingredient selectedIngredient = inventoryIngredientTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        ModifyIngredientController.setInitialModifyIngredient(selectedIngredient);

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyIngredient.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Modify Ingredient Form");
        stage.setScene(scene);
        stage.show();

    }

    public void onDeleteIngredientClick(ActionEvent actionEvent) throws IOException {
        Ingredient selectedIngredient = inventoryIngredientTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        Alert sureDeleteIngredient = new Alert(Alert.AlertType.CONFIRMATION);
        sureDeleteIngredient.setTitle("Ingredients");
        sureDeleteIngredient.setHeaderText("Delete Ingredient: " + selectedIngredient.getName() + "?");
        sureDeleteIngredient.setContentText("Do you want to delete this ingredient?");
        Optional<ButtonType> confirm = sureDeleteIngredient.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        Inventory.deleteIngredient(selectedIngredient);
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void onAddRecipeClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddRecipe.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Recipe Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyRecipeClick(ActionEvent actionEvent) throws IOException {
        Recipe selectedRecipe = inventoryRecipeTable.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null)
            return;

        ModifyRecipeController.setInitialModifyRecipe(selectedRecipe);

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyRecipe.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Modify Recipe Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteRecipeClick(ActionEvent actionEvent) throws IOException {
        Recipe selectedRecipe = inventoryRecipeTable.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null)
            return;

        Alert sureDeleteRecipe = new Alert(Alert.AlertType.CONFIRMATION);
        sureDeleteRecipe.setTitle("Recipes");
        sureDeleteRecipe.setHeaderText("Delete Recipe: " + selectedRecipe.getName() + "?");
        sureDeleteRecipe.setContentText("Do you want to delete this recipe?");
        Optional<ButtonType> confirm = sureDeleteRecipe.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        for(int i = 0; i < selectedRecipe.getAllRequiredIngredients().size(); ++i) {
            Ingredient requiredIngredient = selectedRecipe.getAllRequiredIngredients().get(i);
            if (!selectedRecipe.deleteRequiredIngredient(requiredIngredient)){

                Alert requiredIngredientRecipeError = new Alert(Alert.AlertType.ERROR);
                requiredIngredientRecipeError.setTitle("Required Recipe Ingredient Error");
                requiredIngredientRecipeError.setHeaderText("The recipe named " + selectedRecipe.getName() + ", has an ingredient it requires");
                requiredIngredientRecipeError.setContentText("Please remove " + requiredIngredient.getName() +
                        "\nfrom the recipe before attempting to delete this recipe.\n" +
                        "Recipes with required ingredients cannot be deleted.");
                requiredIngredientRecipeError.showAndWait();
                return;
            }
        }

        Inventory.deleteRecipe(selectedRecipe);
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void ingredientSearchActivate() {
        errorIngredientBox.setText("");
        String searchString = queryIngredients.getText();

        ObservableList<Ingredient> ingredients = Inventory.lookupIngredient(searchString);
        if (ingredients.size() == 0){
            errorIngredientBox.setText("Ingredient not found");
        }
        inventoryIngredientTable.setItems(ingredients);
    }

    public void recipeSearchActivate() {
        errorRecipeBox.setText("");
        String searchString = queryRecipes.getText();

        ObservableList<Recipe> recipes = Inventory.lookupRecipe(searchString);
        if (recipes.size() == 0) {
            errorRecipeBox.setText("Recipe not found");
        }
        inventoryRecipeTable.setItems(recipes);
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }
}