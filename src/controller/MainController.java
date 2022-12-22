package controller;

import com.opencsv.exceptions.CsvException;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import com.opencsv.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    public Label ingredientInventory;
    public TableView<Recipe> inventoryRecipeTable;
    public TableView<Ingredient> inventoryIngredientTable;
    public TableColumn<Ingredient,?> ingredientName;
    public TableColumn<Ingredient,?> ingredientNumberOfUnits;
    public TableColumn<Ingredient, ?> ingredientPriceUnitCombo;
    public Label recipeInventory;
    public TableColumn<Recipe, ?> recipeName;
    public TableColumn<Recipe, ?> recipeServings;
    public TableColumn<Recipe, ?> recipeCost;
    public TextField queryIngredients;
    public TextField queryRecipes;
    public Label errorRecipeBox;
    public Label errorIngredientBox;
    public Label titleLabel;

    private String fileName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ingredientInventory.setText("Ingredients");
        ingredientInventory.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 12));
        recipeInventory.setText("Recipes");
        recipeInventory.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 12));

        for (int i = 0; i < Inventory.getAllIngredients().size(); ++i){
            Ingredient currentIngredient = Inventory.getAllIngredients().get(i);
            currentIngredient.setNumberOfUnits(Inventory.countNumOfUnits(currentIngredient));
        }

        inventoryIngredientTable.setItems(Inventory.getAllIngredients());
        inventoryRecipeTable.setItems(Inventory.getAllRecipes());

        ingredientName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        ingredientNumberOfUnits.setCellValueFactory(new PropertyValueFactory<>("numberOfUnits"));
        ingredientPriceUnitCombo.setCellValueFactory(new PropertyValueFactory<>("concatPriceUnit"));

        resetInformation();

        recipeName.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
        recipeServings.setCellValueFactory(new PropertyValueFactory<>("recipeServings"));
        recipeCost.setCellValueFactory(new PropertyValueFactory<>("recipeCost"));;

    }

    public void resetInformation() {
        for(int i = 0; i < Inventory.getAllRecipes().size(); ++i){ // change this to remove delete ingredient == remove ingredient from recipe
            Recipe redoRecipe = Inventory.getAllRecipes().get(i);
            for(int j = redoRecipe.getAllRequiredIngredients().size() - 1; j >= 0; --j){
                boolean isRequired = false;
                Ingredient oldIngredient = redoRecipe.getAllRequiredIngredients().get(j);
                if (Inventory.getAllIngredients().size() > 0) {
                    for (int p = 0; p < Inventory.getAllIngredients().size(); ++p) {
                        Ingredient newIngredient = Inventory.getAllIngredients().get(p);
                        if (oldIngredient.getId() == newIngredient.getId()) {
                            isRequired = true;
                        }
                        if (oldIngredient.getId() == newIngredient.getId() && (
                                !Objects.equals(oldIngredient.getIngredientName(), newIngredient.getIngredientName()) ||
                                        oldIngredient.getPricePerContainer() != newIngredient.getPricePerContainer() ||
                                        oldIngredient.getNumberOfUnits() != newIngredient.getNumberOfUnits() ||
                                        !Objects.equals(oldIngredient.getUnitOfMeasure(), newIngredient.getUnitOfMeasure()) ||
                                        oldIngredient.getUnitsPerContainer() != newIngredient.getUnitsPerContainer())
                        ) {
                            redoRecipe.removeRequiredIngredient(oldIngredient);
                            redoRecipe.addRequiredIngredient(newIngredient);
                        }

                        if (!isRequired && p == (Inventory.getAllIngredients().size() - 1)) {
                            redoRecipe.removeRequiredIngredient(oldIngredient);
                        }
                    }
                }
                else {
                    redoRecipe.removeRequiredIngredient(oldIngredient);
                }
            }
        }
    }

    @FXML
    public void onAddIngredientClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddIngredient.fxml")));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 300);
        stage.setTitle("Add Ingredient Form");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyIngredientClick(ActionEvent actionEvent) throws IOException {
        Ingredient selectedIngredient = inventoryIngredientTable.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null)
            return;

        ModifyIngredientController.setInitialModifyIngredient(selectedIngredient);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyIngredient.fxml")));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 300);
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
        sureDeleteIngredient.setHeaderText("Delete Ingredient: " + selectedIngredient.getIngredientName() + "?");
        sureDeleteIngredient.setContentText("Do you want to delete this ingredient?");
        Optional<ButtonType> confirm = sureDeleteIngredient.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        Inventory.deleteIngredient(selectedIngredient);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("To Taste: Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void onAddRecipeClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddRecipe.fxml")));
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

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyRecipe.fxml")));
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
        sureDeleteRecipe.setHeaderText("Delete Recipe: " + selectedRecipe.getRecipeName() + "?");
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
                requiredIngredientRecipeError.setHeaderText("The recipe named " + selectedRecipe.getRecipeName() + ", has an ingredient it requires");
                requiredIngredientRecipeError.setContentText("Please remove " + requiredIngredient.getIngredientName() +
                        "\nfrom the recipe before attempting to delete this recipe.\n" +
                        "Recipes with required ingredients cannot be deleted.");
                requiredIngredientRecipeError.showAndWait();
                return;
            }
        }

        Inventory.deleteRecipe(selectedRecipe);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("To Taste: Main Screen");
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

    public void onSaveButtonClick() {
        String loadFileName = getSaveName();
        TextInputDialog saveAsAlert = new TextInputDialog(loadFileName);
        saveAsAlert.setTitle("Save File As");
        saveAsAlert.setHeaderText("Choose a name for your file");
        saveAsAlert.setContentText("What would you like to name your recipe/ingredient list?");
        Optional<String> result = saveAsAlert.showAndWait();

        result.ifPresent(name -> {
            try {
                writeData(name);
                setSaveName(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void setSaveName(String name) {
        fileName = name;
    }
    public String getSaveName(){
        return fileName;
    }

    public void writeData(String saveFileName) throws IOException {
        CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(saveFileName + ".csv"))
                .withSeparator('|')
                .build();
        String[] recipeList = new String[6];
        String[] headerSpace = new String[7];

        for(int i = 0; i < Inventory.getAllRecipes().size(); ++i) {
            headerSpace[0] = "Recipe Name";
            headerSpace[1] = "Recipe Cost";
            headerSpace[2] = "Number of Servings";
            headerSpace[3] = "Flavor Tags";
            writer.writeNext(headerSpace, false);
            Recipe thisRecipe = Inventory.getAllRecipes().get(i);
            recipeList[0] = thisRecipe.getRecipeName();
            recipeList[1] = thisRecipe.getRecipeCost();
            recipeList[2] = String.valueOf(thisRecipe.getRecipeServings());
            recipeList[3] = thisRecipe.getFlavorTags().replace(",","");
            writer.writeNext(recipeList, false);
            String[] ingredientList = new String[7];
            Arrays.fill(headerSpace, " ");
            headerSpace[0] = thisRecipe.getRecipeName() + " -- Required Ingredients";
            writer.writeNext(headerSpace, false);
            for (int j = 0; j < thisRecipe.getAllRequiredIngredients().size(); ++j){
                headerSpace[0] = "Ingredient Id";
                headerSpace[1] = "Ingredient Name";
                headerSpace[2] = "Price Per Container";
                headerSpace[3] = "Number of Units in All Recipes";
                headerSpace[4] = "Unit of Measure";
                headerSpace[5] = "Units Per Container";
                headerSpace[6] = "Expiration Date";
                writer.writeNext(headerSpace, false);
                Ingredient thisIngredient = thisRecipe.getAllRequiredIngredients().get(j);
                ingredientList[0] = String.valueOf(thisIngredient.getId());
                ingredientList[1] = thisIngredient.getIngredientName();
                ingredientList[2] = String.valueOf(thisIngredient.getPricePerContainer());
                ingredientList[3] = String.valueOf(thisIngredient.getNumberOfUnits());
                ingredientList[4] = thisIngredient.getUnitOfMeasure();
                ingredientList[5] = String.valueOf(thisIngredient.getUnitsPerContainer());
                if(thisIngredient instanceof NonPerishable) {
                    ingredientList[6] = ((NonPerishable) thisIngredient).getLongDate();
                }
                if(thisIngredient instanceof Perishable) {
                    ingredientList[6] = ((Perishable) thisIngredient).getShortDate();
                }
                writer.writeNext(ingredientList, false);
            }
            Arrays.fill(headerSpace, " ");
            writer.writeNext(headerSpace, false);
        }
        writer.close();
    }

    public void onLoadButtonClick() throws IOException, CsvException {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        readLoadedFile(selectedFile);
    }
    public void readLoadedFile(File selectedFile) throws IOException, CsvException {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(selectedFile)).build();
            List<String[]> myEntries = reader.readAll();
            ObservableList<Ingredient> ingredientsAsIs = Inventory.getAllIngredients();
            Recipe recipe = null;

            wipeExistingInventory();

            for (int i = 0; i < myEntries.size(); ++i) {
                String currentLine = Arrays.toString(myEntries.get(i));
                currentLine = currentLine.replace("[", "");
                currentLine = currentLine.replace("]", "");

                if (currentLine.contains("$")) {
                    String[] currentArrayLine = currentLine.split("[|]");
                    double fillCost = 0;
                    int fillNumberServings = Integer.parseInt(currentArrayLine[2]);
                    String fillFlavorTags = currentArrayLine[3].replace(" ", ", ");
                    recipe = new Recipe(currentArrayLine[0], fillCost, fillNumberServings, fillFlavorTags);
                    Inventory.addRecipe(recipe);
                    ++i;
                } else if (Character.isDigit(currentLine.charAt(0))) {
                    boolean existingIngredient = false;
                    String[] currentArrayLine = currentLine.split("[|]");
                    for (Ingredient ingredientsAdd : ingredientsAsIs) {
                        if (Objects.equals(currentArrayLine[0], String.valueOf(ingredientsAdd.getId()))) {
                            assert recipe != null;
                            recipe.addRequiredIngredient(ingredientsAdd);
                            existingIngredient = true;
                        }
                    }
                    if (!existingIngredient) {
                        int dubiousId = Integer.parseInt(currentArrayLine[0]); // change name when testing for existing ids
                        double fillPricePerContainer = Double.parseDouble(currentArrayLine[2]);
                        int fillNumOfUnits = Integer.parseInt(currentArrayLine[3]);
                        int fillUnitsContainer = Integer.parseInt(currentArrayLine[5]);
                        if (currentArrayLine[6].length() > 7) {
                            Perishable perishable = new Perishable(dubiousId, currentArrayLine[1], fillPricePerContainer,
                                    fillNumOfUnits, currentArrayLine[4], fillUnitsContainer, currentArrayLine[6]);
                            Inventory.addIngredient(perishable);
                            assert recipe != null;
                            recipe.addRequiredIngredient(perishable);
                        } else {
                            NonPerishable nonPerishable = new NonPerishable(dubiousId, currentArrayLine[1], fillPricePerContainer,
                                    fillNumOfUnits, currentArrayLine[4], fillUnitsContainer, currentArrayLine[6]);
                            Inventory.addIngredient(nonPerishable);
                            assert recipe != null;
                            recipe.addRequiredIngredient(nonPerishable);
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
    public void onClearButtonClick() {
        Alert sureWipeAll = new Alert(Alert.AlertType.CONFIRMATION);
        sureWipeAll.setTitle("Clear Inventory");
        sureWipeAll.setHeaderText("Delete every recipe and ingredient currently in system?");
        sureWipeAll.setContentText("You will be able to load from file to replace deleted items.");
        Optional<ButtonType> confirm = sureWipeAll.showAndWait();

        if (confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
            return;
        }

        wipeExistingInventory();

    }
    public void wipeExistingInventory(){
        while(Inventory.getAllIngredients().size() > 0){
            Inventory.deleteIngredient(Inventory.getAllIngredients().get(0));
        }
        while(Inventory.getAllRecipes().size() > 0){
            Inventory.deleteRecipe(Inventory.getAllRecipes().get(0));
        }
    }

        public void onExitButtonClick() {
        System.exit(0);
    }
}