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
            if (searchedIngredient.getName().contains(ingredientName)) {
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
            if (Objects.equals(searchName, searchedForIngredient.getName())) {
                allIngredients.remove(searchedForIngredient);
                allIngredients.add(ingredient);
                break;
            }
        }
    }

    public static void updateRecipe(int index, Recipe recipe) {
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();
        for (Recipe searchedForRecipe : allRecipes) {
            if (index == searchedForRecipe.getId()) {
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

    /* public static int findNewIngredientId() {
        int maxId = -1;
        ObservableList<Ingredient> allIngredients = Inventory.getAllIngredients();

        for (Ingredient searchedForIngredient : allIngredients) {
            if (maxId <= searchedForIngredient.getId()) {
                maxId = searchedForIngredient.getId();
            }
        }
        return maxId + 1;
    }

     */

    public static int findNewRecipeId() {
        int maxId = -1;
        ObservableList<Recipe> allRecipes = Inventory.getAllRecipes();

        for (Recipe searchedForRecipe : allRecipes) {
            if (maxId <= searchedForRecipe.getId()) {
                maxId = searchedForRecipe.getId();
            }
        }
        return maxId + 1;
    }

    private static void addTestData() {
        Perishable cap = new Perishable("Capacitive Diractors", 12.50, 12, 10, 20, 117);
        Inventory.addIngredient(cap);
        NonPerishable mod = new NonPerishable("Modial Reluctors", 112.99, 10, 5, 15, "Dodge");
        Inventory.addIngredient(mod);
        NonPerishable fam = new NonPerishable("Famulated Amulite", 89.99, 15, 15, 30, "Rockwell");
        Inventory.addIngredient(fam);
        Recipe enc = new Recipe(1001, "Encabulator", 31200.50, 3, 1, 5);
        Inventory.addRecipe(enc);
        Recipe slo = new Recipe(1002, "Slotted Stater", 450.33, 5, 2, 10);
        Inventory.addRecipe(slo);
        Recipe pha = new Recipe(1003, "Phase Detractor", 1232.99, 10, 1, 10);
        Inventory.addRecipe(pha);
    }

    static {
        addTestData();
    }

}
