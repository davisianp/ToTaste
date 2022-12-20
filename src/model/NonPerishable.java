package model;

public class NonPerishable extends Ingredient {

    private String longDate;

    public NonPerishable(int id, String ingredientName, double pricePerContainer,
                         int numberOfUnits, String unitOfMeasure, int unitsPerContainer,
                         String longDate) {
        super(id, ingredientName, pricePerContainer, numberOfUnits, unitOfMeasure, unitsPerContainer);
        this.longDate = longDate;
    }

    public void setLongDate(String longDate){
        this.longDate = longDate;
    }

    public String getLongDate() {
        return longDate;
    }
}
