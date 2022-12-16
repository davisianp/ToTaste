package model;


public abstract class Ingredient {

    private int id;
    private String ingredientName;
    private double pricePerEach;
    private int numberOfUnits;
    private String unitOfMeasure;
    private int servingsPerContainer;

    public Ingredient(int id, String ingredientName, double pricePerEach, int numberOfUnits, String unitOfMeasure, int servingsPerContainer) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.pricePerEach = pricePerEach;
        this.numberOfUnits = numberOfUnits;
        this.unitOfMeasure = unitOfMeasure;
        this.servingsPerContainer = servingsPerContainer;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
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

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
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

    public String getConcatPriceUnit() {
        return "$" + String.format("%.2f", pricePerEach) + " per " + unitOfMeasure;
    }

}