import java.util.*;

public class PhoneSearchService {
    public static void searchPhone(Scanner sc) {
        if (PhoneManager.phones.isEmpty()) {
            System.out.println("No phones available.");
            return;
        }

        String key = "";
        boolean fieldSelected = false;

        while (true) {
            // Step 1: Choose search field (only once unless changed)
            if (!fieldSelected) {
                System.out.print("\nSearch by (id/brand/model/storage/price/condition/seller): ");
                key = sc.nextLine().trim().toLowerCase();

                // Check for valid key
                if (!List.of("id", "brand", "model", "storage", "price", "condition", "seller").contains(key)) {
                    System.out.println("Invalid field. Try again.");
                    continue;
                }
                fieldSelected = true;
            }

            // Step 2: Ask for search value
            System.out.print("Enter value: ");
            String value = sc.nextLine().trim();

            boolean foundAny = false;

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

                if (found) {
                    foundAny = true;
                    System.out.println("\nFound: " + p);
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
                    break; // Stop after first match
                }
            }

            if (!foundAny) {
                // No match found - ask user what to do
                System.out.println("\n>>> No match found\n");
                System.out.println("1. Try a new value \t\t 2. Change search field \t\t 3. Cancel search");
                System.out.print("\n>>> Enter choice (1-3): ");
                String next = sc.nextLine().trim();

                switch (next) {
                    case "1" -> {
                        continue; // same field, ask new value
                    }
                    case "2" -> {
                        fieldSelected = false; // reset field
                        continue;
                    }
                    case "3" -> {
                        System.out.println("Search canceled.");
                        return;
                    }
                    default -> System.out.println("Invalid option. Search canceled.");
                }
                return;
            } else {
                break; // match found, exit
            }
        }
    }
}
