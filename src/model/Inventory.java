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

    /*public static Ingredient lookupIngredient(int recipeId) {
        ObservableList<Ingredient> allIngredients = Inventory.getAllIngredients();

        for (Ingredient searchedForIngredient : allIngredients) {
            if (recipeId == searchedForIngredient.getName()) {
                return searchedForIngredient;
            }
        }
        return null;
    }

    public static Recipe lookupRecipe(int recipeId) {
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();

        for (Recipe searchedForRecipe : allRecipes) {
            if (recipeId == searchedForRecipe.getId()) {
                return searchedForRecipe;
            }
        }
        return null;
    }

     */

    public static ObservableList<Ingredient> lookupIngredient(String ingredientName) {
        ObservableList<Ingredient> ingredientNames = FXCollections.observableArrayList();
        ObservableList<Ingredient> allIngredients = Inventory.getAllIngredients();
        for (Ingredient searchedIngredient : allIngredients) {
            if (searchedIngredient.getIngredientName().contains(ingredientName)) {
                ingredientNames.add(searchedIngredient);
            }
        }
        return ingredientNames;
    }

    public static ObservableList<Recipe> lookupRecipe(String ingredientName) {
        ObservableList<Recipe> recipeNames = FXCollections.observableArrayList();
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();
        for (Recipe searchedRecipe : allRecipes) {
            if (searchedRecipe.getName().contains(ingredientName)) {
                recipeNames.add(searchedRecipe);
            }
        }
        return recipeNames;
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
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();
        for (Recipe searchedForRecipe : allRecipes) {
            if (Objects.equals(searchName, searchedForRecipe.getName())) {
                allRecipes.remove(searchedForRecipe);
                allRecipes.add(recipe);
                break;
            }
        }
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
        Perishable cap = new Perishable("Smoked Sausage", 4.50, 2, "lbs", 1, "01/21/2023");
        Inventory.addIngredient(cap);
        NonPerishable mod = new NonPerishable("Red Kidney Beans", 2.00, 4, "tbsps", 1, "02/24");
        Inventory.addIngredient(mod);
        NonPerishable fam = new NonPerishable("Long Grain Rice", 3.00, 1, "tsps", 20, "03/25");
        Inventory.addIngredient(fam);
        Recipe enc = new Recipe("Encabulator", 31200.50, 3, 1);
        Inventory.addRecipe(enc);
        Recipe slo = new Recipe("Slotted Stater", 450.33, 5, 2);
        Inventory.addRecipe(slo);
        Recipe pha = new Recipe("Phase Detractor", 1232.99, 10, 1);
        Inventory.addRecipe(pha);
    }

    static {
        addTestData();
    }

}
