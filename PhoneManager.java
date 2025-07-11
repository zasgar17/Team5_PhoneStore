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

        int newCount = 0;
        int usedCount = 0;

        System.out.printf("%-6s %-15s %-15s %-12s %-12s %-15s %-20s %-15s\n",
            "ID", "Brand", "Model", "Storage", "Price", "Condition", "Seller Name", "Seller Phone");
        System.out.println("------------------------------------------------------------------------------------------------------------");

        for (Phone p : phones) {
            System.out.printf("%-6d %-15s %-15s %-12d %-12.2f %-15s %-20s %-15s\n",
                p.id, p.brand, p.model, p.storage, p.price, p.condition, p.seller.name, p.seller.phone);

            if (p.condition.equalsIgnoreCase("new")) newCount++;
            else if (p.condition.equalsIgnoreCase("used")) usedCount++;
        }

        System.out.println("\n>>>\tTotal phones: " + phones.size());
        System.out.println(">>>\tNew: " + newCount + " | Used: " + usedCount);
    }

    public static void editPhone(Scanner sc, Phone p) {
    System.out.print("New Brand [" + p.brand + "]: ");
    String brand = sc.nextLine();
    if (!brand.isEmpty()) p.brand = brand;

    System.out.print("New Model [" + p.model + "]: ");
    String model = sc.nextLine();
    if (!model.isEmpty()) p.model = model;

    while (true) {
        System.out.print("New Storage [" + p.storage + "]: ");
        String storage = sc.nextLine();
        if (storage.isEmpty()) break;
        try {
            p.storage = Integer.parseInt(storage);
            break;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Try again.");
        }
    }

    while (true) {
        System.out.print("New Price [" + p.price + "]: ");
        String price = sc.nextLine();
        if (price.isEmpty()) break;
        try {
            p.price = Double.parseDouble(price);
            break;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Try again.");
        }
    }

    System.out.print("New Condition [" + p.condition + "]: ");
    String condition = sc.nextLine();
    if (!condition.isEmpty()) p.condition = condition;

    System.out.print("New Seller Name [" + p.seller.name + "]: ");
    String sname = sc.nextLine();
    if (!sname.isEmpty()) p.seller.name = sname;

    System.out.print("New Seller Phone [" + p.seller.phone + "]: ");
    String sphone = sc.nextLine();
    if (!sphone.isEmpty()) p.seller.phone = sphone;

    System.out.println("Phone updated.");
}


    public static void sortPhones(Scanner sc) {
    System.out.print("Sort by (id/brand/model/price/storage/condition/seller): ");
    String field = sc.nextLine().toLowerCase();

    System.out.print("Order (asc/desc): ");
    String order = sc.nextLine().toLowerCase();
    boolean ascending = order.equals("asc");

    Comparator<Phone> comparator = switch (field) {
        case "id" -> Comparator.comparingInt(p -> p.id);
        case "brand" -> Comparator.comparing(p -> p.brand.toLowerCase());
        case "model" -> Comparator.comparing(p -> p.model.toLowerCase());
        case "price" -> Comparator.comparingDouble(p -> p.price);
        case "storage" -> Comparator.comparingInt(p -> p.storage);
        case "condition" -> Comparator.comparing(p -> p.condition.toLowerCase());
        case "seller" -> Comparator.comparing(p -> p.seller.name.toLowerCase());
        default -> null;
    };

    if (comparator == null) {
        System.out.println("Invalid sort field.");
        return;
    }

    if (!ascending) comparator = comparator.reversed();
    phones.sort(comparator);

    System.out.println("Sorted by " + field + " (" + (ascending ? "ASC" : "DESC") + ")");
    listPhones();
}


   public static void filterPhones(Scanner sc) {
    System.out.println("--- FILTER PHONES ---");
    System.out.println("Leave blank to skip a field.");

    System.out.print("Brand: ");
    String brand = sc.nextLine();
    System.out.print("Model: ");
    String model = sc.nextLine();
    System.out.print("Condition (New/Used): ");
    String condition = sc.nextLine();
    System.out.print("Min Price: ");
    String minPriceStr = sc.nextLine();
    System.out.print("Max Price: ");
    String maxPriceStr = sc.nextLine();
    System.out.print("Storage (GB): ");
    String storageStr = sc.nextLine();
    System.out.print("Seller Name: ");
    String sellerName = sc.nextLine();

    double minPrice = minPriceStr.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPriceStr);
    double maxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);
    int storage = storageStr.isEmpty() ? -1 : Integer.parseInt(storageStr);

    int count = 0, newCount = 0, usedCount = 0;

    System.out.printf("%-6s %-15s %-15s %-12s %-12s %-15s %-20s %-15s\n",
        "ID", "Brand", "Model", "Storage", "Price", "Condition", "Seller Name", "Seller Phone");
    System.out.println("------------------------------------------------------------------------------------------------------------");

    for (Phone p : phones) {
        if (!brand.isEmpty() && !p.brand.equalsIgnoreCase(brand)) continue;
        if (!model.isEmpty() && !p.model.equalsIgnoreCase(model)) continue;
        if (!condition.isEmpty() && !p.condition.equalsIgnoreCase(condition)) continue;
        if (p.price < minPrice || p.price > maxPrice) continue;
        if (storage != -1 && p.storage != storage) continue;
        if (!sellerName.isEmpty() && !p.seller.name.equalsIgnoreCase(sellerName)) continue;

        System.out.printf("%-6d %-15s %-15s %-12d %-12.2f %-15s %-20s %-15s\n",
            p.id, p.brand, p.model, p.storage, p.price, p.condition, p.seller.name, p.seller.phone);

        count++;
        if (p.condition.equalsIgnoreCase("new")) newCount++;
        else if (p.condition.equalsIgnoreCase("used")) usedCount++;
    }

    if (count == 0) {
        System.out.println("No matching phones found.");
    } else {
        System.out.println("\nFiltered phones: " + count);
        System.out.println("New: " + newCount + " | Used: " + usedCount);
    }
}
}