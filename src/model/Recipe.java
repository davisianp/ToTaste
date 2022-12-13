package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class Recipe {
    private ObservableList<Ingredient> requiredIngredients = FXCollections.observableArrayList();
    private String recipeName;
    private double recipeCost;
    private int recipeServings;
    private String flavorTags;

    public Recipe(String recipeName, double recipeCost,
                  int recipeServings, String flavorTags){
        this.recipeName = recipeName;
        this.recipeCost = recipeCost;
        this.recipeServings = recipeServings;
        this.flavorTags = flavorTags;
    }

    public void setRecipeName(String recipeName){ this.recipeName = recipeName; }

    public void setRecipeServings(int recipeServings){ this.recipeServings = recipeServings; }

    public void setFlavorTags(String flavorTags){ this.flavorTags = flavorTags; }

    public String getRecipeName() {
        return recipeName;
    }

    public int getRecipeServings() {
        return recipeServings;
    }

    public String getRecipeCost() {
        recipeCost = 0;
        for (Ingredient requiredIngredient : requiredIngredients) {
            recipeCost += requiredIngredient.getPricePerEach();
        }
        return "$" + String.format("%.2f", recipeCost);
    }

    public String getFlavorTags() {
        return flavorTags;
    }


    public void addRequiredIngredient(Ingredient ingredient){
        requiredIngredients.add(ingredient);
    }
    public void removeRequiredIngredient(Ingredient ingredient) { requiredIngredients.remove(ingredient); }

    public boolean deleteRequiredIngredient(Ingredient selectedRequiredIngredient){
        for (int i = 0; i < Inventory.getAllRecipes().size(); ++i) {
            Recipe checkRecipe = Inventory.getAllRecipes().get(i);
            if (Objects.equals(checkRecipe.getRecipeName(), recipeName)) {
                for (int j = 0; j < checkRecipe.getAllRequiredIngredients().size(); ++j) {
                    if (selectedRequiredIngredient == checkRecipe.requiredIngredients.get(j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ObservableList<Ingredient> getAllRequiredIngredients() {
        return requiredIngredients;
    }
}
