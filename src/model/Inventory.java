package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class Inventory {
    private static final ObservableList<Ingredient> ALL_INGREDIENTS = FXCollections.observableArrayList();
    private static final ObservableList<Recipe> ALL_RECIPES = FXCollections.observableArrayList();

    public static void addIngredient(Ingredient ingredient) {
        ALL_INGREDIENTS.add(ingredient);
    }

    public static void addRecipe(Recipe recipe) {
        ALL_RECIPES.add(recipe);
    }

    public static ObservableList<Ingredient> lookupIngredient(String ingredientName) {
        ObservableList<Ingredient> ingredientNames = FXCollections.observableArrayList();
        ObservableList<Ingredient> allIngredients = Inventory.getAllIngredients();
        for (Ingredient searchedIngredient : allIngredients) {
            if (searchedIngredient.getIngredientName().toLowerCase().contains(ingredientName.toLowerCase())) {
                ingredientNames.add(searchedIngredient);
            }
        }
        return ingredientNames;
    }

    public static ObservableList<Recipe> lookupRecipe(String recipeName) {
        ObservableList<Recipe> recipeSearch = FXCollections.observableArrayList();
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();
        for (Recipe searchedRecipe : allRecipes) {
            String[] searchedRecipeFlavorTags = searchedRecipe.getFlavorTags().split(",\s");
            boolean noRepeat = true;
            for (String searchedRecipeFlavorTag : searchedRecipeFlavorTags) {
                if (recipeName.equalsIgnoreCase(searchedRecipeFlavorTag)) {
                    recipeSearch.add(searchedRecipe);
                    if (searchedRecipe.getRecipeName().equalsIgnoreCase(searchedRecipeFlavorTag)){
                        noRepeat = false;
                    }
                    break;
                }
            }
            if (searchedRecipe.getRecipeName().toLowerCase().contains(recipeName.toLowerCase()) && noRepeat){
                recipeSearch.add(searchedRecipe);
            }
        }
        return recipeSearch;
    }

    public static void updateIngredient(String searchName, Ingredient ingredient) {
        ObservableList<Ingredient> allIngredients = Inventory.getAllIngredients();
        for (Ingredient searchedForIngredient : allIngredients) {
            if (Objects.equals(searchName, searchedForIngredient.getIngredientName())) {
                allIngredients.remove(searchedForIngredient);
                allIngredients.add(ingredient);
                break;
            }
        }
    }

    public static void updateRecipe(String searchName, Recipe recipe) {

        System.out.println(searchName);
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();
        for (Recipe searchedForRecipe : allRecipes) {
            if (Objects.equals(searchName, searchedForRecipe.getRecipeName())) {
                allRecipes.remove(searchedForRecipe);
                allRecipes.add(recipe);
                break;
            }
        }
    }

    public static int countNumOfUnits(Ingredient ingredient) {
        int countNum = 0;
        for (int i = 0; i < getAllRecipes().size(); ++i){
            Recipe checkRecipe = getAllRecipes().get(i);
            for (int j = 0; j < checkRecipe.getAllRequiredIngredients().size(); ++j){
                if (ingredient.getId() == checkRecipe.getAllRequiredIngredients().get(j).getId()){
                    countNum += 1;
                }
            }
            System.out.println(checkRecipe.getRecipeName() + " " + ingredient.getId() + " " + ingredient.getIngredientName());
        }
        return countNum;
    }

    public static void deleteIngredient(Ingredient ingredient) {
        ALL_INGREDIENTS.remove(ingredient);
    }

    public static void deleteRecipe(Recipe recipe) {
        ALL_RECIPES.remove(recipe);
    }

    public static ObservableList<Ingredient> getAllIngredients() {
        return ALL_INGREDIENTS;
    }

    public static ObservableList<Recipe> getAllRecipes() {
        return ALL_RECIPES;
    }

    private static void addTestData() {
        Perishable sage = new Perishable(1,"Smoked Sausage", 4.50, 0, "lbs", 1, "01/21/2023");
        Inventory.addIngredient(sage);
        NonPerishable bean = new NonPerishable(2,"Red Kidney Beans", 2.00, 0, "can", 1, "02/2024");
        Inventory.addIngredient(bean);
        NonPerishable rice = new NonPerishable(3,"Long Grain Rice", 3.00, 0, "quarter cup", 20, "03/2025");
        Inventory.addIngredient(rice);
        Recipe rbar = new Recipe("Red Beans and Rice", 0, 4, "spicy, zesty, smoky");
        Inventory.addRecipe(rbar);
        rbar.addRequiredIngredient(sage);
        rbar.addRequiredIngredient(bean);
        rbar.addRequiredIngredient(rice);
        Recipe jamb = new Recipe("Jambalaya", 0, 4, "zesty, meaty, hearty");
        Inventory.addRecipe(jamb);
        jamb.addRequiredIngredient(sage);
        jamb.addRequiredIngredient(rice);
        Recipe soup = new Recipe("15 Bean Soup", 0, 4, "hammy, rich, hearty");
        Inventory.addRecipe(soup);
        soup.addRequiredIngredient(bean);
    }

    public static int pickNewId(){
        int checkId = -1;
        for(int i = 0; i < getAllIngredients().size(); ++i){
            Ingredient ingredientAtIndex = getAllIngredients().get(i);
            if (checkId <= ingredientAtIndex.getId()){
                checkId = ingredientAtIndex.getId();
            }
        }
        return checkId + 1;
    }

    static {
        addTestData();
    }
}
