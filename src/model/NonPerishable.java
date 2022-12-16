package model;

public class NonPerishable extends Ingredient {

    private String longDate;

    public NonPerishable(int id, String ingredientName, double pricePerEach,
                         int numberOfUnits, String unitOfMeasure, int servingsPerContainer,
                         String longDate) {
        super(id, ingredientName, pricePerEach, numberOfUnits, unitOfMeasure, servingsPerContainer);
        this.longDate = longDate;
    }

    public void setLongDate(String longDate){
        this.longDate = longDate;
    }

    public String getLongDate() {
        return longDate;
    }
}
