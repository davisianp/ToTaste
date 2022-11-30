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
    public Label partsInventory;
    public TableView<Recipe> inventoryProductTable;
    public TableView<Ingredient> inventoryPartTable;
    public Label productInventory;
    public TableColumn<Ingredient,?> partId;
    public TableColumn<Ingredient,?> partName;
    public TableColumn<Ingredient,?> partInventoryLevel;
    public TableColumn<Ingredient,?> partPriceCostPerItem;
    public TableColumn<Recipe, ?> productId;
    public TableColumn<Recipe, ?> productName;
    public TableColumn<Recipe, ?> productInventoryLevel;
    public TableColumn<Recipe, ?> productPriceCostPerItem;
    public TextField queryParts;
    public TextField queryProducts;
    public Label addProductLabel;
    public Label errorProductBox;
    public Label errorPartBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsInventory.setText("Parts");
        partsInventory.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 12));
        productInventory.setText("Products");
        productInventory.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 12));

        inventoryPartTable.setItems(Inventory.getAllIngredients());
        inventoryProductTable.setItems(Inventory.getAllRecipes());

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostPerItem.setCellValueFactory(new PropertyValueFactory<>("price"));

        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCostPerItem.setCellValueFactory(new PropertyValueFactory<>("price"));

        inventoryPartTable.getSortOrder().add(partId);
        inventoryProductTable.getSortOrder().add(productId);
    }

    @FXML
    public void onAddIngredientClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add Part Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyIngredientClick(ActionEvent actionEvent) throws IOException {
        Ingredient selectedIngredient = inventoryPartTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        ModifyIngredientController.setInitialModifyIngredient(selectedIngredient);

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Modify Part Form");
        stage.setScene(scene);
        stage.show();

    }

    public void onDeleteIngredientClick(ActionEvent actionEvent) throws IOException {
        Ingredient selectedIngredient = inventoryPartTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        Alert sureDeletePart = new Alert(Alert.AlertType.CONFIRMATION);
        sureDeletePart.setTitle("Parts");
        sureDeletePart.setHeaderText("Delete Part ID " + selectedIngredient.getId() + "?");
        sureDeletePart.setContentText("Do you want to delete this part?");
        Optional<ButtonType> confirm = sureDeletePart.showAndWait();

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
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Product Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyRecipeClick(ActionEvent actionEvent) throws IOException {
        Recipe selectedRecipe = inventoryProductTable.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null)
            return;

        ModifyRecipeController.setInitialModifyRecipe(selectedRecipe);

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Modify Product Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteRecipeClick(ActionEvent actionEvent) throws IOException {
        Recipe selectedRecipe = inventoryProductTable.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null)
            return;

        Alert sureDeleteProduct = new Alert(Alert.AlertType.CONFIRMATION);
        sureDeleteProduct.setTitle("Products");
        sureDeleteProduct.setHeaderText("Delete Product ID " + selectedRecipe.getId() + "?");
        sureDeleteProduct.setContentText("Do you want to delete this product?");
        Optional<ButtonType> confirm = sureDeleteProduct.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        for(int i = 0; i < selectedRecipe.getAllRequiredIngredients().size(); ++i) {
            Ingredient requiredIngredient = selectedRecipe.getAllRequiredIngredients().get(i);
            if (!selectedRecipe.deleteRequiredIngredient(requiredIngredient)){

                Alert associatedPartProductError = new Alert(Alert.AlertType.ERROR);
                associatedPartProductError.setTitle("Product Associated Part Error");
                associatedPartProductError.setHeaderText("The product, ID " + selectedRecipe.getId() + ", has a part associated with it");
                associatedPartProductError.setContentText("Please remove associated part ID " + requiredIngredient.getId() +
                        "\nfrom the product before attempting to delete this product.\n" +
                        "Products with associated parts cannot be deleted.");
                associatedPartProductError.showAndWait();
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
        errorPartBox.setText("");
        String searchString = queryParts.getText();

        ObservableList<Ingredient> ingredients = Inventory.lookupIngredient(searchString);
        if (ingredients.size() == 0){
            try {
                int searchInt = Integer.parseInt(searchString);
                Ingredient searchIngredient = Inventory.lookupIngredient(searchInt);
                if (searchIngredient != null) {
                    ingredients.add(searchIngredient);
                }
                else {
                    errorPartBox.setText("Cannot find part ID number");
                }
            }
            catch(NumberFormatException e) {
                errorPartBox.setText("Part not found");
            }
        }
        inventoryPartTable.setItems(ingredients);
    }

    public void recipeSearchActivate() {
        errorProductBox.setText("");
        String searchString = queryProducts.getText();

        ObservableList<Recipe> recipes = Inventory.lookupRecipe(searchString);
        if (recipes.size() == 0) {
            try {
                int searchInt = Integer.parseInt(searchString);
                Recipe searchRecipe = Inventory.lookupRecipe(searchInt);
                if (searchRecipe != null) {
                    recipes.add(searchRecipe);
                }
                else {
                    errorProductBox.setText("Cannot find product ID number");
                }
            }
            catch(NumberFormatException e) {
                errorProductBox.setText("Product not found");
            }
        }
        inventoryProductTable.setItems(recipes);
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }
}