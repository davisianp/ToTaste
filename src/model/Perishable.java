package model;

public class Perishable extends Ingredient {

    private String shortDate;

    public Perishable(int id, String ingredientName, double pricePerContainer,
                      int numberOfUnits, String unitOfMeasure, int unitsPerContainer,
                      String shortDate) {
        super(id, ingredientName, pricePerContainer, numberOfUnits, unitOfMeasure, unitsPerContainer);
        this.shortDate = shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getShortDate() {
        return shortDate;
    }
}
