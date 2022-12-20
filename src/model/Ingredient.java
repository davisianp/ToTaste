package model;


public abstract class Ingredient {

    private int id;
    private String ingredientName;
    private double pricePerContainer;
    private int numberOfUnits;
    private String unitOfMeasure;
    private int unitsPerContainer;

    public Ingredient(int id, String ingredientName, double pricePerContainer, int numberOfUnits, String unitOfMeasure, int unitsPerContainer) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.pricePerContainer = pricePerContainer;
        this.numberOfUnits = numberOfUnits;
        this.unitOfMeasure = unitOfMeasure;
        this.unitsPerContainer = unitsPerContainer;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getPricePerContainer() {
        return pricePerContainer;
    }

    public void setPricePerContainer(double pricePerContainer) {
        this.pricePerContainer = pricePerContainer;
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

    public int getUnitsPerContainer() {
        return unitsPerContainer;
    }

    public void setUnitsPerContainer(int unitsPerContainer) {
        this.unitsPerContainer = unitsPerContainer;
    }

    public String getConcatPriceUnit() {
        double refinePrice = (pricePerContainer / unitsPerContainer);
        return "$" + String.format("%.2f", refinePrice) + " per " + unitOfMeasure;
    }

}