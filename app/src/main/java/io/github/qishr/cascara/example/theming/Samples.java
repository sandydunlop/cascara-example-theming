package io.github.qishr.cascara.example.theming;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.github.qishr.cascara.common.diagnostic.GlobalReporter;
import io.github.qishr.cascara.common.diagnostic.Reporter;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class Samples {
    private static final Reporter REPORTER = GlobalReporter.forClass(Samples.class);

    @SuppressWarnings("rawtypes")
    ListView listView;
    @SuppressWarnings("rawtypes")
    TableView tableView;
    @SuppressWarnings("rawtypes")
    TreeView treeView;
    @SuppressWarnings("rawtypes")
    TreeTableView treeTableView;

    private VBox view;

    public Samples() {
        view = new VBox(16);
        view.setPadding(new Insets(0,0,0,0));

        TabPane tabs = new TabPane();

        Tab controls = new Tab("Controls");
        controls.setContent(buildControlShowcase());
        controls.setClosable(false);
        tabs.getTabs().add(controls);

        Tab lists = new Tab("Lists");
        lists.setContent(buildTablesAndTreesShowcase());
        lists.setClosable(false);
        tabs.getTabs().add(lists);

        Tab boxes = new Tab("Boxes");
        boxes.setContent(buildBoxesShowcase());
        boxes.setClosable(false);
        tabs.getTabs().add(boxes);

        view.getChildren().addAll(tabs);
    }

    public VBox getView() { return view; }

    public void printComputedCssValuesFixed(Node node) {

        REPORTER.info("--- Computed CSS Values for Node: " + node.getClass().getSimpleName() + " ---");

        // 1. Iterate using raw types (or the less restrictive wildcard form)
        for (CssMetaData<?, ?> metaData : node.getCssMetaData()) {
            try {
                // 2. Cast the metaData to its most basic Styleable form.
                //    This is an unchecked operation, but is necessary due to type erasure
                //    and the structure of the JavaFX CSS internal API.
                @SuppressWarnings("unchecked")
                CssMetaData<Styleable, ?> styleableMetaData = (CssMetaData<Styleable, ?>) metaData;

                // 3. Now, call the method, casting the Node to the required Styleable interface
                @SuppressWarnings("unchecked")
                StyleableProperty<Object> styleableProperty =
                    (StyleableProperty<Object>) styleableMetaData.getStyleableProperty((Styleable) node);

                if (styleableProperty != null) {
                    Object computedValue = styleableProperty.getValue();

                    REPORTER.info(
                        "%-25s: %s",
                        metaData.getProperty(),
                        computedValue
                    );
                } else {
                    REPORTER.info("%-25s: (Property instance not found)%n", metaData.getProperty());
                }
            } catch (Exception e) {
                // If the property lookup fails (e.g., internal error), log the exception
                REPORTER.error(null, "Error fetching %s: %s%n", metaData.getProperty(), e.getMessage());
            }
        }

        REPORTER.info("--------------------------------------------------");
    }

    public VBox buildBoxesShowcase() {
        Label test = new Label("Test");

        TitledPane titledPane = new TitledPane();
        titledPane.setText("Text");
        titledPane.setContent(test);

        VBox box = new VBox();
        box.setSpacing(20);
        box.setPadding(new Insets(10));
        box.getChildren().addAll(titledPane);
        return box;
    }

    /**
     * Creates a VBox containing a GridPane with all controls.
     * @return The VBox container.
     */
    public VBox buildControlShowcase() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        // grid.setPadding(new Insets(15));
        grid.setAlignment(Pos.TOP_CENTER);
        // grid.setBackground(Background.fill(Paint.valueOf("#1d2e28")));

        // --- Helper for creating a titled TitledPane for each control ---
        int row = 0;
        int col = 0;

        // The controls list is defined here. We will iterate through them to place them in the grid.
        Node[] controls = new Node[] {
            // Row 0
            createHyperlink(),
            createLabel(),
            new Button("Button"),
            createMenuButton(),

            // Row 1
            createSplitMenuButton(),
            new ToggleButton("Toggle Button"),
            createRadioButton(),
            createCheckBox(),

            // Row 2
            createChoiceBox(),
            createComboBox(),
            createDatePicker(),
            new ColorPicker(),

            // Row 3
            new TextField("Text Field"),
            new PasswordField(),
            createSlider(),
            createProgressBar(),
        };

        // --- Populate the 4x4 Grid ---
        for (Node control : controls) {
            TitledPane titledPane = new TitledPane(
                control.getClass().getSimpleName(), // Use class name as title
                control
            );
            titledPane.setCollapsible(false); // Prevents collapsing

            // Set max width to force controls to stretch and align in the grid
            titledPane.setMaxWidth(Double.MAX_VALUE);
            // grid.setBackground(Background.fill(Paint.valueOf("#14452f")));

            // Ensure the control itself takes up space
            if (control instanceof Control) {
                ((Control)control).setMaxWidth(Double.MAX_VALUE);
            }

            grid.add(titledPane, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        // --- VBox Setup ---
        VBox box = new VBox();
        box.getChildren().add(grid);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(20);
        box.setPadding(new Insets(10));

        // vBox.setPadding(new Insets(10));

        return box;
    }

    private Node createHyperlink() {
        Hyperlink hyperlink = new Hyperlink("Hyperlink");
        hyperlink.setId("myHyperlink");
        hyperlink.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myHyperlink");
            printComputedCssValuesFixed(node);
        });
        return hyperlink;
    }

    private Node createLabel() {
        Label label = new Label("label");
        label.setId("myLabel");
        label.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myLabel");
            printComputedCssValuesFixed(node);
        });
        return label;
    }

    private Node createRadioButton() {
        RadioButton radioButton = new RadioButton("Radio Button");
        radioButton.setId("myRadionButton");
        radioButton.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myRadionButton");
            printComputedCssValuesFixed(node);
        });
        return radioButton;
    }
    private Node createCheckBox() {
        CheckBox checkBox = new CheckBox("Check Box");
        checkBox.setId("myCheckBox");
        checkBox.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myCheckBox");
            printComputedCssValuesFixed(node);
        });
        return checkBox;
    }

    // --- Control Creation Helpers ---
    private Node createMenuButton() {
        MenuButton mb = new MenuButton("MenuButton");
        mb.getItems().add(new MenuItem("Item 1"));
        return mb;
    }

    private Node createSplitMenuButton() {
        SplitMenuButton smb = new SplitMenuButton(new MenuItem("Item 1"));
        smb.setText("SplitMenuButton");
        return smb;
    }

    private Node createChoiceBox() {
        ChoiceBox<String> cb = new ChoiceBox<>();
        cb.getItems().addAll(Arrays.asList("Choice A", "Choice B"));
        cb.setValue("Choice A");
        cb.setId("myChoiceBox");
        cb.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myChoiceBox");
            printComputedCssValuesFixed(node);
        });
        return cb;
    }

    private Node createComboBox() {
        ComboBox<String> cbb = new ComboBox<>();
        cbb.getItems().addAll(Arrays.asList("Combo 1", "Combo 2"));
        cbb.setValue("Combo 1");
        cbb.setId("myComboBox");
        cbb.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myComboBox");
            printComputedCssValuesFixed(node);
        });
        return cbb;
    }

    private Node createDatePicker() {
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setId("myDatePicker");
        datePicker.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#myDatePicker");
            printComputedCssValuesFixed(node);
        });
        return datePicker;
    }

    private Node createSlider() {
        Slider slider = new Slider(0, 100, 50);
        slider.setId("mySlider");
        slider.setShowTickLabels(true);
        slider.setOnMouseClicked(mouse-> {
            Node node = view.lookup("#mySlider");
            printComputedCssValuesFixed(node);
        });
        return slider;
    }

    private Node createProgressBar() {
        ProgressBar pb = new ProgressBar(0.6);
        return pb;
    }



    private VBox buildTablesAndTreesShowcase() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        listView = buildListView();
        tableView = buildTableView();
        treeView = buildTreeView();
        treeTableView = buildTreeTableView();
        gridPane.add(listView, 0, 0, 1, 1);
        gridPane.add(tableView, 1, 0, 1, 1);
        gridPane.add(treeView, 0, 1, 1, 1);
        gridPane.add(treeTableView, 1, 1, 1, 1);
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        HBox.setHgrow(gridPane, Priority.ALWAYS);


        CheckBox cellCheckBox = new CheckBox("Cell Selection");
        cellCheckBox.setOnAction(action -> {
            tableView.getSelectionModel().setCellSelectionEnabled(cellCheckBox.isSelected());
            treeTableView.getSelectionModel().setCellSelectionEnabled(cellCheckBox.isSelected());
        });

        VBox samples = new VBox(cellCheckBox, gridPane);
        samples.setSpacing(20);
        samples.setPadding(new Insets(10));
        return samples;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ListView buildListView() {
        ListView view = new ListView<>();
        view.getItems().add("Item 1");
        view.getItems().add("Item 2");
        view.getItems().add("Item 3");
        return view;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private TableView buildTableView() {
        TableView view = new TableView<>();

        // 1. Instantiate the Manager
        // CellReferenceManager<Person, String> manager = new CellReferenceManager<>();

        TableColumn<Person, String> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Person, String> column2 = new TableColumn<>("Last Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        view.getColumns().add(column1);
        view.getColumns().add(column2);
        view.getItems().add(new Person("John", "Doe"));
        view.getItems().add(new Person("Jane", "Deer"));

        // AbstractPage page = this;
        view.setId("myTable");

        return view;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private TreeView buildTreeView() {
        TreeItem rootItem = new TreeItem("Tutorials");

        TreeItem webItem = new TreeItem("Web Tutorials");
        webItem.getChildren().add(new TreeItem("HTML  Tutorial"));
        webItem.getChildren().add(new TreeItem("HTML5 Tutorial"));
        webItem.getChildren().add(new TreeItem("CSS Tutorial"));
        webItem.getChildren().add(new TreeItem("SVG Tutorial"));
        rootItem.getChildren().add(webItem);

        TreeItem javaItem = new TreeItem("Java Tutorials");
        javaItem.getChildren().add(new TreeItem("Java Language"));
        javaItem.getChildren().add(new TreeItem("Java Collections"));
        javaItem.getChildren().add(new TreeItem("Java Concurrency"));
        rootItem.getChildren().add(javaItem);

        TreeView treeView = new TreeView();
        treeView.setRoot(rootItem);
        return treeView;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private TreeTableView buildTreeTableView() {
        TreeTableView<Car> treeTableView = new TreeTableView<Car>();

        TreeTableColumn<Car, String> treeTableColumn1 = new TreeTableColumn<>("Brand");
        TreeTableColumn<Car, String> treeTableColumn2 = new TreeTableColumn<>("Model");

        treeTableColumn1.setCellValueFactory(new TreeItemPropertyValueFactory<>("brand"));
        treeTableColumn2.setCellValueFactory(new TreeItemPropertyValueFactory<>("model"));

        treeTableView.getColumns().add(treeTableColumn1);
        treeTableView.getColumns().add(treeTableColumn2);

        TreeItem mercedes1 = new TreeItem(new Car("Mercedes", "SL500"));
        TreeItem mercedes2 = new TreeItem(new Car("Mercedes", "SL500 AMG"));
        // TreeItem mercedes3 = new TreeItem(new Car("Mercedes", "CLA 200"));

        TreeItem mercedes = new TreeItem(new Car("Mercedes", "..."));
        mercedes.getChildren().add(mercedes1);
        mercedes.getChildren().add(mercedes2);

        TreeItem audi1 = new TreeItem(new Car("Audi", "A1"));
        TreeItem audi2 = new TreeItem(new Car("Audi", "A5"));
        TreeItem audi3 = new TreeItem(new Car("Audi", "A7"));

        TreeItem audi = new TreeItem(new Car("Audi", "..."));
        audi.getChildren().add(audi1);
        audi.getChildren().add(audi2);
        audi.getChildren().add(audi3);

        TreeItem cars = new TreeItem(new Car("Cars", "..."));
        cars.getChildren().add(audi);
        cars.getChildren().add(mercedes);

        treeTableView.setRoot(cars);

        return treeTableView;
    }

    public static class Person {
        String firstName = "";
        String lastName = "";

        public Person(String fn, String ln) {
            firstName = fn;
            lastName = ln;
        }

        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    public class Car {

        private String brand = null;
        private String model = null;

        public Car() {
        }

        public Car(String brand, String model) {
            this.brand = brand;
            this.model = model;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }



    // S=Person, T=String for column1
    public class CellReferenceManager<S, T> {

        // Key: Row Index (0 for Row 0), Value: The actual TableCell Node
        private final Map<Integer, TableCell<S, T>> visibleCells = new HashMap<>();

        public TableCell<S, T> createCell() {
            return new TableCell<S, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);

                    this.setMaxHeight(0); // Applies to the TableCell itself
                    this.setWrapText(false); // Ensure no wrapping unless specifically needed

                    if (empty || item == null) {
                        // Cell is recycled or empty; remove reference
                        visibleCells.remove(getIndex());
                        setText(null);
                    } else {
                        // Cell is visible; store the reference
                        visibleCells.put(getIndex(), this);
                        setText(item.toString());
                    }
                }
            };
        }

        public TableCell<S, T> getCellNode(int rowIndex) {
            // Will return the TableCell if it's currently visible
            return visibleCells.get(rowIndex);
        }
    }
}
