import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        PhoneStoreUI ui = new PhoneStoreUI();
        Scene scene = new Scene(ui.getMainView(), 800, 600);

        primaryStage.setTitle("Phone Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
