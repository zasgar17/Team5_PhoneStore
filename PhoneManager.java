import java.util.*;

public class PhoneManager {
    public static ArrayList<Phone> phones = new ArrayList<>();

    public static void addPhone(Scanner sc) {
        try {
            System.out.print("ID: "); int id = Integer.parseInt(sc.nextLine());
            System.out.print("Brand: "); String brand = sc.nextLine();
            System.out.print("Model: "); String model = sc.nextLine();
            System.out.print("Storage (GB): "); int storage = Integer.parseInt(sc.nextLine());
            System.out.print("Price: "); double price = Double.parseDouble(sc.nextLine());
            System.out.print("Condition (New/Used): "); String condition = sc.nextLine();
            System.out.print("Seller Name: "); String sname = sc.nextLine();
            System.out.print("Seller Phone: "); String sphone = sc.nextLine();

            phones.add(new Phone(id, brand, model, storage, price, condition, new Seller(sname, sphone)));
            System.out.println("Phone added.");
        } catch (Exception e) {
            System.out.println("Invalid input. Try again.");
        }
    }

    public static void listPhones() {
        if (phones.isEmpty()) {
            System.out.println("No phones available.");
            return;
        }

        System.out.printf("%-6s %-15s %-15s %-12s %-12s %-15s %-20s %-15s\n",
    "ID", "Brand", "Model", "Storage", "Price", "Condition", "Seller Name", "Seller Phone");
System.out.println("------------------------------------------------------------------------------------------------------------");

for (Phone p : phones) {
    System.out.printf("%-6d %-15s %-15s %-12d %-12.2f %-15s %-20s %-15s\n",
        p.id, p.brand, p.model, p.storage, p.price, p.condition, p.seller.name, p.seller.phone);
}

    }

    public static void editPhone(Scanner sc, Phone p) {
        System.out.print("New Brand [" + p.brand + "]: "); String brand = sc.nextLine();
        System.out.print("New Model [" + p.model + "]: "); String model = sc.nextLine();
        System.out.print("New Storage [" + p.storage + "]: "); String storage = sc.nextLine();
        System.out.print("New Price [" + p.price + "]: "); String price = sc.nextLine();
        System.out.print("New Condition [" + p.condition + "]: "); String condition = sc.nextLine();
        System.out.print("New Seller Name [" + p.seller.name + "]: "); String sname = sc.nextLine();
        System.out.print("New Seller Phone [" + p.seller.phone + "]: "); String sphone = sc.nextLine();

        if (!brand.isEmpty()) p.brand = brand;
        if (!model.isEmpty()) p.model = model;
        if (!storage.isEmpty()) p.storage = Integer.parseInt(storage);
        if (!price.isEmpty()) p.price = Double.parseDouble(price);
        if (!condition.isEmpty()) p.condition = condition;
        if (!sname.isEmpty()) p.seller.name = sname;
        if (!sphone.isEmpty()) p.seller.phone = sphone;

        System.out.println("Phone updated.");
    }

    public static void sortPhones(Scanner sc) {
        System.out.print("Sort by (brand/model/price/storage): ");
        String field = sc.nextLine().toLowerCase();

        phones.sort((p1, p2) -> switch (field) {
            case "brand" -> p1.brand.compareToIgnoreCase(p2.brand);
            case "model" -> p1.model.compareToIgnoreCase(p2.model);
            case "price" -> Double.compare(p1.price, p2.price);
            case "storage" -> Integer.compare(p1.storage, p2.storage);
            default -> 0;
        });

        System.out.println("Sorted by " + field + "!\n");
        listPhones();
    }

    public static void filterPhones(Scanner sc) {
        System.out.println("--- FILTER PHONES ---");
        System.out.println("Leave blank to skip a field.");

        System.out.print("Brand: "); String brand = sc.nextLine();
        System.out.print("Model: "); String model = sc.nextLine();
        System.out.print("Condition (New/Used): "); String condition = sc.nextLine();
        System.out.print("Min Price: "); String minPriceStr = sc.nextLine();
        System.out.print("Max Price: "); String maxPriceStr = sc.nextLine();

        double minPrice = minPriceStr.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPriceStr);
        double maxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);

        boolean found = false;

        System.out.printf("%-4s %-10s %-10s %-8s %-10s %-10s %-15s %-15s\n",
            "ID", "Brand", "Model", "Storage", "Price", "Condition", "Seller Name", "Seller Phone");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Phone p : phones) {
            if (!brand.isEmpty() && !p.brand.equalsIgnoreCase(brand)) continue;
            if (!model.isEmpty() && !p.model.equalsIgnoreCase(model)) continue;
            if (!condition.isEmpty() && !p.condition.equalsIgnoreCase(condition)) continue;
            if (p.price < minPrice || p.price > maxPrice) continue;

            System.out.printf("%-4d %-10s %-10s %-8d %-10.2f %-10s %-15s %-15s\n",
                p.id, p.brand, p.model, p.storage, p.price, p.condition, p.seller.name, p.seller.phone);
            found = true;
        }

        if (!found) System.out.println("No matching phones found.");
    }
}
