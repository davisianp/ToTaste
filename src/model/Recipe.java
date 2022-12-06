package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class Recipe {
    private ObservableList<Ingredient> requiredIngredients = FXCollections.observableArrayList();
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;


    public Recipe(String name, double price,
                  int stock, int min){
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
    }

    public void setName(String name){ this.name = name; }

    public void setPrice(double price){ this.price = price; }


    public void setStock(int stock){ this.stock = stock; }

    public void setMin(int min){ this.min = min; }

    public void setMax(int max){ this.max = max; }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void addRequiredIngredient(Ingredient ingredient){
        requiredIngredients.add(ingredient);
    }

    public boolean deleteRequiredIngredient(Ingredient selectedRequiredIngredient){
        for (int i = 0; i < Inventory.getAllRecipes().size(); ++i) {
            Recipe checkRecipe = Inventory.getAllRecipes().get(i);
            if (Objects.equals(checkRecipe.getName(), name)) {
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
