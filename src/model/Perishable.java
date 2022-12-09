package model;

public class Perishable extends Ingredient {

    private String shortDate;

    public Perishable(int id, String ingredientName, double pricePerEach,
                      int stock, String unitOfMeasure, int servingsPerContainer,
                      String shortDate) {
        super(id, ingredientName, pricePerEach, stock, unitOfMeasure, servingsPerContainer);
        this.shortDate = shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getShortDate() {
        return shortDate;
    }
}
