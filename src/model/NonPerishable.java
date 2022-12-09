package model;

public class NonPerishable extends Ingredient {

    private String longDate;

    public NonPerishable(int id, String ingredientName, double pricePerEach,
                         int stock, String unitOfMeasure, int servingsPerContainer,
                         String longDate) {
        super(id, ingredientName, pricePerEach, stock, unitOfMeasure, servingsPerContainer);
        this.longDate = longDate;
    }

    public void setLongDate(String longDate){
        this.longDate = longDate;
    }

    public String getLongDate() {
        return longDate;
    }
}
