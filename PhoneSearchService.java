import java.util.*;

public class PhoneSearchService {
    public static void searchPhone(Scanner sc) {
        if (PhoneManager.phones.isEmpty()) {
            System.out.println("No phones available.");
            return;
        }

        System.out.print("Search by (id/brand/model/storage/price/condition/seller): ");
        String key = sc.nextLine().toLowerCase();
        System.out.print("Enter value: ");
        String value = sc.nextLine();

        boolean foundAny = false;

        Iterator<Phone> it = PhoneManager.phones.iterator();
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

                switch (action) {
                    case "e" -> PhoneManager.editPhone(sc, p);
                    case "d" -> {
                        PhoneManager.phones.remove(p);
                        System.out.println("Phone deleted.");
                    }
                    default -> System.out.println("Skipped.");
                }
                break;
            }
        }

        if (!foundAny) System.out.println("No match found.");
    }
}
