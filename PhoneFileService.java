import java.io.*;
import java.util.*;

public class PhoneFileService {
    static final String FILE_NAME = "phones.txt";

    public static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(FILE_NAME)) {
            for (Phone p : PhoneManager.phones) {
                pw.println(p.id + ";" + p.brand + ";" + p.model + ";" + p.storage + ";" + p.price + ";" +
                        p.condition + ";" + p.seller.name + ";" + p.seller.phone);
            }
            System.out.println("Saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (Scanner fileScanner = new Scanner(f)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(";");
                PhoneManager.phones.add(new Phone(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]),
                        Double.parseDouble(parts[4]), parts[5], new Seller(parts[6], parts[7])));
            }
        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }
}
