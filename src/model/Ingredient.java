package model;


public abstract class Ingredient {

    private String ingredientName;
    private double pricePerEach;
    private int stock;
    private String unitOfMeasure;
    private int servingsPerContainer;
    public Ingredient(String ingredientName, double pricePerEach, int stock, String unitOfMeasure, int servingsPerContainer) {
        this.ingredientName = ingredientName;
        this.pricePerEach = pricePerEach;
        this.stock = stock;
        this.unitOfMeasure = unitOfMeasure;
        this.servingsPerContainer = servingsPerContainer;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getPricePerEach() {
        return pricePerEach;
    }

    public void setPricePerEach(double pricePerEach) {
        this.pricePerEach = pricePerEach;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public int getServingsPerContainer() {
        return servingsPerContainer;
    }

    public void setServingsPerContainer(int servingsPerContainer) {
        this.servingsPerContainer = servingsPerContainer;
    }

}