import java.util.Scanner;

public class PhoneStoreApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PhoneFileService.loadFromFile();

        while (true) {
            System.out.println("\n========================================================");
            System.out.println("\t\tWelcome to PhonetiCode");
            System.out.println("========================================================");
            System.out.println("\t1>> Add Phone\t\t 2>> List Phones\n");
            System.out.println("\t3>> Search Phone\t 4>> Sort Phones\n");
            System.out.println("\t5>> Filter Phones\t 6>> Total num of phones\n");
            System.out.println("\t\t   7>> Save & Exit\n");

            System.out.println("=========================================================");
            System.out.print("Choose an option (1-7): ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> PhoneManager.addPhone(sc);
                case "2" -> PhoneManager.listPhones();
                case "3" -> PhoneSearchService.searchPhone(sc);
                case "4" -> PhoneManager.sortPhones(sc);
                case "5" -> PhoneManager.filterPhones(sc);
                case "6" -> System.out.println("Total phones in store: " + PhoneManager.phones.size());
                case "7" ->{
                    System.out.println("\nSaving data... Goodbye!");
                    PhoneFileService.saveToFile();
                    return; // Exit program
                }
                default -> System.out.println("Invalid choice! Please enter a number between 1 and 6.");
            }

            // Ask if user wants to continue or quit
            System.out.print("\nDo you want to return to the home page? (yes/no): ");
            String again = sc.nextLine().trim().toLowerCase();
           if (again.equals("no")) {
                System.out.println("\nSaving data... Goodbye!");
                PhoneFileService.saveToFile();
                break;
            }

        }        sc.close();

    }
}
