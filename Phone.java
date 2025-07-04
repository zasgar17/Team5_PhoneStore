public class Phone {
    int id;
    String brand;
    String model;
    int storage;
    double price;
    String condition; // New / Used
    Seller seller;

    public Phone(int id, String brand, String model, int storage, double price, String condition, Seller seller) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.storage = storage;
        this.price = price;
        this.condition = condition;
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Brand: " + brand + ", Model: " + model + ", Storage: " + storage + "GB, $" + price +
                ", Condition: " + condition + ", Seller: " + seller;
    }
}
