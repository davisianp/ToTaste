package model;

public class NonPerishable extends Ingredient {

    private String companyName;

    public NonPerishable(String name, double price, int stock,
                         int min, int max, String companyName) {
        super(name, price, stock, min, max);
        this.companyName = companyName;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
