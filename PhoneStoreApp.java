import java.util.*;
import java.io.*;

class Seller {
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

class Phone {
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

public class PhoneStoreApp {
    static ArrayList<Phone> phones = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "phones.txt";

    public static void main(String[] args) {
        loadFromFile();

        while (true) {
            System.out.println("\n--- PHONE STORE ---");
            System.out.println("Total Phones: " + phones.size());
            System.out.println("1. Add Phone\n2. List Phones\n3. Search Phone\n4. Sort Phones\n5. Filter Phones\n6. Save & Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": addPhone(); break;
                case "2": listPhones(); break;
                case "3": searchPhone(); break;
                case "4": sortPhones(); break;
                case "5": filterPhones(); break;
                case "6": saveToFile(); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static void addPhone() {
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

    static void listPhones() {
        if (phones.isEmpty()) {
            System.out.println("No phones available.");
            return;
        }
        for (Phone p : phones)
            System.out.println(p);
    }

    static void searchPhone() {
        if (phones.isEmpty()) {
            System.out.println("No phones available.");
            return;
        }

        System.out.print("Search by (id/brand/model/storage/price/condition/seller): ");
        String key = sc.nextLine().toLowerCase();
        System.out.print("Enter value: ");
        String value = sc.nextLine();

        Iterator<Phone> it = phones.iterator();
        boolean foundAny = false;

        while (it.hasNext()) {
            Phone p = it.next();
            boolean found = switch (key) {
                case "id" -> Integer.toString(p.id).equals(value);
                case "brand" -> p.brand.equalsIgnoreCase(value);
                case "model" -> p.model.equalsIgnoreCase(value);
                case "storage" -> Integer.toString(p.storage).equals(value);
                case "price" -> Double.toString(p.price).equals(value);
                case "condition" -> p.condition.equalsIgnoreCase(value);
                case "seller" -> p.seller.name.equalsIgnoreCase(value);
                default -> false;
            };

            if (found) {
                foundAny = true;
                System.out.println("Found: " + p);
                System.out.print("Choose: (E)dit / (D)elete / (S)kip: ");
                String action = sc.nextLine().toLowerCase();

                if (action.equals("e")) {
                    editPhone(p);
                } else if (action.equals("d")) {
                    phones.remove(p);
                    System.out.println("Phone deleted.");
                } else {
                    System.out.println("Skipped.");
                }
                break; // Only show one result at a time
            }
        }

        if (!foundAny) System.out.println("No match found.");
    }

    static void editPhone(Phone p) {
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

    static void sortPhones() {
        System.out.print("Sort by (brand/model/price/storage): ");
        String field = sc.nextLine().toLowerCase();

        phones.sort((p1, p2) -> switch (field) {
            case "brand" -> p1.brand.compareToIgnoreCase(p2.brand);
            case "model" -> p1.model.compareToIgnoreCase(p2.model);
            case "price" -> Double.compare(p1.price, p2.price);
            case "storage" -> Integer.compare(p1.storage, p2.storage);
            default -> 0;
        });

        System.out.println("Sorted!");
    }

    static void filterPhones() {
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

        double minPrice = minPriceStr.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPriceStr);
        double maxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);

        List<Phone> filtered = new ArrayList<>();

        for (Phone p : phones) {
            if (!brand.isEmpty() && !p.brand.equalsIgnoreCase(brand)) continue;
            if (!model.isEmpty() && !p.model.equalsIgnoreCase(model)) continue;
            if (!condition.isEmpty() && !p.condition.equalsIgnoreCase(condition)) continue;
            if (p.price < minPrice || p.price > maxPrice) continue;
            filtered.add(p);
        }

        if (filtered.isEmpty()) {
            System.out.println("No phones match the filter.");
        } else {
            for (Phone p : filtered)
                System.out.println(p);
        }
    }

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(FILE_NAME)) {
            for (Phone p : phones) {
                pw.println(p.id + ";" + p.brand + ";" + p.model + ";" + p.storage + ";" + p.price + ";" +
                        p.condition + ";" + p.seller.name + ";" + p.seller.phone);
            }
            System.out.println("Saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (Scanner fileScanner = new Scanner(f)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(";");
                phones.add(new Phone(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]),
                        Double.parseDouble(parts[4]), parts[5], new Seller(parts[6], parts[7])));
            }
        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }
}