import java.util.Scanner;

public class PhoneStoreApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PhoneFileService.loadFromFile();

        while (true) {
            System.out.println("\n--- PHONE STORE ---");
            System.out.println("Total Phones: " + PhoneManager.phones.size());
            System.out.println("1. Add Phone\n2. List Phones\n3. Search Phone\n4. Sort Phones\n5. Filter Phones\n6. Save & Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> PhoneManager.addPhone(sc);
                case "2" -> PhoneManager.listPhones();
                case "3" -> PhoneSearchService.searchPhone(sc);
                case "4" -> PhoneManager.sortPhones(sc);
                case "5" -> PhoneManager.filterPhones(sc);
                case "6" -> {
                    PhoneFileService.saveToFile();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
