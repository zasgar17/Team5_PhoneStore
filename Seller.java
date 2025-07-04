public class Seller {
    String name;
    String phone;

    public Seller(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
