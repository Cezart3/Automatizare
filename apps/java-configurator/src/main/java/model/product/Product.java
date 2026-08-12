package model.product;

public class Product {
    private String codProdus;
    private String denumire;

    public Product(String codProdus, String denumire) {
        this.codProdus = codProdus;
        this.denumire = denumire;
    }

    public String getCodProdus() {
        return codProdus;
    }

    public String getDenumire() {
        return denumire;
    }

    @Override
    public String toString() {
        return codProdus + " - " + denumire;
    }
}
