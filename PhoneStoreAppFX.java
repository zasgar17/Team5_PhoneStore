// === New File: PhoneStoreAppFX.java ===
import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PhoneStoreAppFX extends Application {
    private TableView<Phone> table = new TableView<>();
    private ObservableList<Phone> data;

    @Override
    public void start(Stage primaryStage) {
        PhoneFileService.loadFromFile();
        data = FXCollections.observableArrayList(PhoneManager.phones);

        primaryStage.setTitle("PhonetiCode - Phone Store");

        // === Table columns ===
        TableColumn<Phone, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Phone, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Phone, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Phone, Integer> storageCol = new TableColumn<>("Storage");
        storageCol.setCellValueFactory(new PropertyValueFactory<>("storage"));

        TableColumn<Phone, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Phone, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Phone, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(cellData -> {
            Seller seller = cellData.getValue().seller;
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(() -> seller.toString()));
        });

        // === Actions column ===
        TableColumn<Phone, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setOnAction(event -> {
                    Phone p = getTableView().getItems().get(getIndex());
                    showEditDialog(p);
                });
                deleteBtn.setOnAction(event -> {
                    Phone p = getTableView().getItems().get(getIndex());
                    PhoneManager.phones.remove(p);
                    data.remove(p);
                });
                HBox pane = new HBox(5, editBtn, deleteBtn);
                pane.setAlignment(Pos.CENTER);
                setGraphic(pane);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });

        table.getColumns().addAll(idCol, brandCol, modelCol, storageCol, priceCol, conditionCol, sellerCol, actionCol);
        table.setItems(data);

        // === Add form ===
        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField brandField = new TextField(); brandField.setPromptText("Brand");
        TextField modelField = new TextField(); modelField.setPromptText("Model");
        TextField storageField = new TextField(); storageField.setPromptText("Storage");
        TextField priceField = new TextField(); priceField.setPromptText("Price");
        TextField conditionField = new TextField(); conditionField.setPromptText("Condition");
        TextField sellerNameField = new TextField(); sellerNameField.setPromptText("Seller Name");
        TextField sellerPhoneField = new TextField(); sellerPhoneField.setPromptText("Seller Phone");

        Button addBtn = new Button("Add Phone");
        addBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String brand = brandField.getText();
                String model = modelField.getText();
                int storage = Integer.parseInt(storageField.getText());
                double price = Double.parseDouble(priceField.getText());
                String condition = conditionField.getText();
                String sellerName = sellerNameField.getText();
                String sellerPhone = sellerPhoneField.getText();

                Phone newPhone = new Phone(id, brand, model, storage, price, condition, new Seller(sellerName, sellerPhone));
                PhoneManager.phones.add(newPhone);
                data.add(newPhone);

                idField.clear(); brandField.clear(); modelField.clear();
                storageField.clear(); priceField.clear(); conditionField.clear();
                sellerNameField.clear(); sellerPhoneField.clear();
            } catch (Exception ex) {
                showAlert("Invalid input! Please check your fields.");
            }
        });

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> PhoneFileService.saveToFile());

        HBox form = new HBox(5, idField, brandField, modelField, storageField,
                priceField, conditionField, sellerNameField, sellerPhoneField, addBtn, saveBtn);
        form.setPadding(new Insets(10));
        form.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, table, form);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    }

    private void showEditDialog(Phone p) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Phone");

        TextField brandField = new TextField(p.brand);
        TextField modelField = new TextField(p.model);
        TextField storageField = new TextField(String.valueOf(p.storage));
        TextField priceField = new TextField(String.valueOf(p.price));
        TextField conditionField = new TextField(p.condition);
        TextField sellerNameField = new TextField(p.seller.name);
        TextField sellerPhoneField = new TextField(p.seller.phone);

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.addRow(0, new Label("Brand:"), brandField);
        grid.addRow(1, new Label("Model:"), modelField);
        grid.addRow(2, new Label("Storage:"), storageField);
        grid.addRow(3, new Label("Price:"), priceField);
        grid.addRow(4, new Label("Condition:"), conditionField);
        grid.addRow(5, new Label("Seller Name:"), sellerNameField);
        grid.addRow(6, new Label("Seller Phone:"), sellerPhoneField);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveBtn) {
                p.brand = brandField.getText();
                p.model = modelField.getText();
                p.storage = Integer.parseInt(storageField.getText());
                p.price = Double.parseDouble(priceField.getText());
                p.condition = conditionField.getText();
                p.seller.name = sellerNameField.getText();
                p.seller.phone = sellerPhoneField.getText();
                table.refresh();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}