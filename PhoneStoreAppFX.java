import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PhoneStoreAppFX extends Application {
    private TableView<Phone> table = new TableView<>();
    private ObservableList<Phone> data;
    private FilteredList<Phone> filteredData;
    private Label countLabel = new Label();


    @Override
    public void start(Stage primaryStage) {
        PhoneFileService.loadFromFile();
        data = FXCollections.observableArrayList(PhoneManager.phones);
        filteredData = new FilteredList<>(data, p -> true);

        primaryStage.setTitle("PhonetiCode - Phone Store");

        // === Table Columns ===
        TableColumn<Phone, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().id).asObject());

        TableColumn<Phone, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().brand));

        TableColumn<Phone, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().model));

        TableColumn<Phone, Integer> storageCol = new TableColumn<>("Storage (GB)");
        storageCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().storage).asObject());

        TableColumn<Phone, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().price).asObject());

        TableColumn<Phone, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().condition));

        TableColumn<Phone, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().seller.name + " (" + cellData.getValue().seller.phone + ")"
        ));

        // === Actions column ===
        TableColumn<Phone, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                pane.setAlignment(Pos.CENTER);

                editBtn.setOnAction(event -> {
                    Phone p = getTableView().getItems().get(getIndex());
                    showEditDialog(p);
                });

                deleteBtn.setOnAction(event -> {
                    Phone p = getTableView().getItems().get(getIndex());
                    PhoneManager.phones.remove(p);
                    data.remove(p);
                    filteredData.setPredicate(filteredData.getPredicate());
                    table.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        table.getColumns().addAll(idCol, brandCol, modelCol, storageCol, priceCol, conditionCol, sellerCol, actionCol);

        SortedList<Phone> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        countLabel.setText("Total Phones: " + sortedData.size());
        sortedData.addListener((javafx.collections.ListChangeListener<Phone>) change -> {
            countLabel.setText("Total Phones: " + sortedData.size());
        });


        // Count label update
        countLabel.setText("Total Phones: " + filteredData.size());
        
        filteredData.predicateProperty().addListener((obs, oldPred, newPred) -> 
            countLabel.setText("Total Phones: " + filteredData.size()));

        HBox tableHeader = new HBox(countLabel);
        tableHeader.setPadding(new Insets(0, 10, 5, 10));
        tableHeader.setAlignment(Pos.CENTER_RIGHT);



        // === Filter Form ===
        Label filterLabel = new Label("FILTER PHONES:");
        filterLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TextField filterBrandField = new TextField();
        filterBrandField.setPromptText("Brand");

        TextField filterModelField = new TextField();
        filterModelField.setPromptText("Model");

        TextField filterConditionField = new TextField();
        filterConditionField.setPromptText("New/Used");

        TextField filterMinPriceField = new TextField();
        filterMinPriceField.setPromptText("Min Price");

        TextField filterMaxPriceField = new TextField();
        filterMaxPriceField.setPromptText("Max Price");

        TextField filterStorageField = new TextField();
        filterStorageField.setPromptText("Storage (GB)");

        TextField filterSellerField = new TextField();
        filterSellerField.setPromptText("Seller Name");

        Button applyFilterBtn = new Button("Apply Filter");
        applyFilterBtn.setOnAction(e -> applyFilters(
            filterBrandField.getText(),
            filterModelField.getText(),
            filterConditionField.getText(),
            filterMinPriceField.getText(),
            filterMaxPriceField.getText(),
            filterStorageField.getText(),
            filterSellerField.getText()
        ));

        Button clearFilterBtn = new Button("Clear Filter");
        clearFilterBtn.setOnAction(e -> {
            filterBrandField.clear();
            filterModelField.clear();
            filterConditionField.clear();
            filterMinPriceField.clear();
            filterMaxPriceField.clear();
            filterStorageField.clear();
            filterSellerField.clear();
            filteredData.setPredicate(p -> true);
        });

        HBox filterForm = new HBox(5,
            filterBrandField, filterModelField, filterConditionField,
            filterMinPriceField, filterMaxPriceField, filterStorageField,
            filterSellerField, applyFilterBtn, clearFilterBtn
        );
        filterForm.setPadding(new Insets(5));
        filterForm.setAlignment(Pos.CENTER);

        // === Add form ===
        Label addLabel = new Label("ADD NEW PHONE:");
        addLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField brandField = new TextField();
        brandField.setPromptText("Brand");

        TextField modelField = new TextField();
        modelField.setPromptText("Model");

        TextField storageField = new TextField();
        storageField.setPromptText("Storage (GB)");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField conditionField = new TextField();
        conditionField.setPromptText("New/Used");

        TextField sellerNameField = new TextField();
        sellerNameField.setPromptText("Seller Name");

        TextField sellerPhoneField = new TextField();
        sellerPhoneField.setPromptText("Seller Phone");

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

                if (brand.isEmpty() || model.isEmpty() || condition.isEmpty() ||
                    sellerName.isEmpty() || sellerPhone.isEmpty()) {
                    showAlert("All fields are required!");
                    return;
                }

                Phone newPhone = new Phone(id, brand, model, storage, price, condition,
                        new Seller(sellerName, sellerPhone));
                PhoneManager.phones.add(newPhone);
                data.add(newPhone);
                filteredData.setPredicate(filteredData.getPredicate());
                table.refresh();

                idField.clear();
                brandField.clear();
                modelField.clear();
                storageField.clear();
                priceField.clear();
                conditionField.clear();
                sellerNameField.clear();
                sellerPhoneField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Invalid number format! Check ID, Storage, Price.");
            }
        });

        Button saveBtn = new Button("Save Data");
        saveBtn.setOnAction(e -> {
            PhoneFileService.saveToFile();
            showAlert("Data saved successfully to phones.txt");
        });

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> {
            PhoneFileService.loadFromFile();
            data.setAll(PhoneManager.phones);
            filteredData.setPredicate(filteredData.getPredicate());
            table.refresh();
        });

        HBox addForm = new HBox(5, idField, brandField, modelField, storageField,
                priceField, conditionField, sellerNameField, sellerPhoneField, addBtn, saveBtn, refreshBtn);
        addForm.setPadding(new Insets(5));
        addForm.setAlignment(Pos.CENTER);

        VBox filterSection = new VBox(5, filterLabel, filterForm);
        filterSection.setAlignment(Pos.CENTER);

        VBox addSection = new VBox(5, addLabel, addForm);
        addSection.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, tableHeader, table, filterSection, addSection);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 1200, 650));
        primaryStage.show();
    }

    private void applyFilters(String brand, String model, String condition,
                              String minPriceStr, String maxPriceStr,
                              String storageStr, String sellerName) {

        filteredData.setPredicate(phone -> {
            if (!brand.isEmpty() && !phone.brand.equalsIgnoreCase(brand)) return false;
            if (!model.isEmpty() && !phone.model.equalsIgnoreCase(model)) return false;
            if (!condition.isEmpty() && !phone.condition.equalsIgnoreCase(condition)) return false;

            try {
                double minPrice = minPriceStr.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPriceStr);
                double maxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);
                if (phone.price < minPrice || phone.price > maxPrice) return false;
            } catch (NumberFormatException e) {
                showAlert("Invalid price format!");
                return false;
            }

            if (!storageStr.isEmpty()) {
                try {
                    int storage = Integer.parseInt(storageStr);
                    if (phone.storage != storage) return false;
                } catch (NumberFormatException e) {
                    showAlert("Invalid storage format!");
                    return false;
                }
            }

            if (!sellerName.isEmpty() && !phone.seller.name.equalsIgnoreCase(sellerName)) return false;

            return true;
        });
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
        grid.addRow(2, new Label("Storage (GB):"), storageField);
        grid.addRow(3, new Label("Price:"), priceField);
        grid.addRow(4, new Label("Condition:"), conditionField);
        grid.addRow(5, new Label("Seller Name:"), sellerNameField);
        grid.addRow(6, new Label("Seller Phone:"), sellerPhoneField);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveBtn) {
                try {
                    p.brand = brandField.getText();
                    p.model = modelField.getText();
                    p.storage = Integer.parseInt(storageField.getText());
                    p.price = Double.parseDouble(priceField.getText());
                    p.condition = conditionField.getText();
                    p.seller.name = sellerNameField.getText();
                    p.seller.phone = sellerPhoneField.getText();
                    filteredData.setPredicate(filteredData.getPredicate());
                    table.refresh();
                } catch (NumberFormatException e) {
                    showAlert("Invalid format for storage or price!");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}