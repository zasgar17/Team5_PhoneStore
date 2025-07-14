import java.util.*;

public class PhoneSearchService {
    public static void searchPhone(Scanner sc) {
        if (PhoneManager.phones.isEmpty()) {
            System.out.println("No phones available.");
            return;
        }

        String key = "";

        while (true) {
            System.out.print("\nSearch by (id/brand/model/storage/price/condition/seller): ");
            key = sc.nextLine().trim().toLowerCase();

            if (!List.of("id", "brand", "model", "storage", "price", "condition", "seller").contains(key)) {
                System.out.println("Invalid field. Try again.");
                continue;
            }
            break;
        }

        System.out.print("Enter value: ");
        String value = sc.nextLine().trim();

        List<Phone> matches = new ArrayList<>();
        for (Phone p : PhoneManager.phones) {
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

            if (found) matches.add(p);
        }

        if (matches.isEmpty()) {
            System.out.println("\n>>> No matches found.");
            return;
        }

        System.out.println("\nFound " + matches.size() + " matching phone(s):");

        Iterator<Phone> iterator = matches.iterator();
        while (iterator.hasNext()) {
            Phone p = iterator.next();
            System.out.println("\n" + p);

            System.out.print("Choose: (E)dit / (D)elete / (S)kip: ");
            String action = sc.nextLine().trim().toLowerCase();

            switch (action) {
                case "e" -> PhoneManager.editPhone(sc, p);
                case "d" -> {
                    PhoneManager.phones.remove(p);
                    System.out.println("Phone deleted.");
                }
                default -> System.out.println("Skipped.");
            }
        }
    }
}