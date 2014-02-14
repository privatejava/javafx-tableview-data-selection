/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tableviewdataselection;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Narayan G. Maharjan <me@ngopal.com.np>
 */
public class TableViewDataDemoController {
    /**
     * For the data transformation
     */
    public static DataFormat dataFormat = new DataFormat("mydata");

    @FXML
    private ToggleGroup selectionGrp;

    @FXML
    private ComboBox<TableColumn<Person, ?>> colSelect;

    @FXML
    private RadioButton cellRadio, rowRadio;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Integer> listView;

    @FXML
    private TableColumn<Person, String> addressCol;

    @FXML
    private TableColumn<Person, Gender> genderCol;

    @FXML
    private TableColumn<Person, String> nameCol;

    @FXML
    private TableColumn<Person, String> phoneCol;

    @FXML
    private TableColumn<Person, String> snCol;

    @FXML
    private TableView<Person> tableView;

    ObservableList<Integer> selectedIndexes = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert addressCol != null : "fx:id=\"addressCol\" was not injected: check your FXML file 'TableViewDataFXML.fxml'.";
        assert genderCol != null : "fx:id=\"genderCol\" was not injected: check your FXML file 'TableViewDataFXML.fxml'.";
        assert nameCol != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'TableViewDataFXML.fxml'.";
        assert phoneCol != null : "fx:id=\"phoneCol\" was not injected: check your FXML file 'TableViewDataFXML.fxml'.";
        assert snCol != null : "fx:id=\"snCol\" was not injected: check your FXML file 'TableViewDataFXML.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'TableViewDataFXML.fxml'.";

        // changed to multiple selection mode
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // set cell value factories
        setCellValueFactories();

        //set Dummy Data for the TableView
        tableView.setItems(getData());

        //ListView items bound with selection index property of tableview
        listView.setItems(selectedIndexes);

        //change listview observable list
        tableView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> change) {
                selectedIndexes.setAll(change.getList());
            }
        });

        //Setting the items for columns selection
        colSelect.setItems(tableView.getColumns());
        for (TableColumn c : colSelect.getItems()) {
            setCellColumnSelection(c);
        }

        //add listener and update of selection type
        colSelect.valueProperty().addListener(new ChangeListener<TableColumn<Person, ?>>() {
            @Override
            public void changed(
                      ObservableValue<? extends TableColumn<Person, ?>> ov,
                      TableColumn<Person, ?> t,
                      final TableColumn<Person, ?> t1) {
                if (t1 != null) {
                    if (cellRadio.isSelected()) {
                        setCellSelection();
                    }
                }

            }
        });


        //For showing the column name properly
        colSelect.setConverter(new StringConverter<TableColumn<Person, ?>>() {
            @Override
            public String toString(TableColumn<Person, ?> t) {
                return t.getText();
            }

            @Override
            public TableColumn<Person, ?> fromString(String string) {
                for (TableColumn<Person, ?> t : colSelect.getItems()) {
                    if (t.getText().equals(string)) {
                        return t;
                    }
                }
                return null;
            }
        });

        //the radio buttons change property listener [Row/Cell] selection
        selectionGrp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(
                      ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                if (t1 == cellRadio) {
                    setCellSelection();
                } else {
                    setRowSelection();
                }

            }
        });
        //Stricting the Column selection
        colSelect.disableProperty().bind(cellRadio.selectedProperty().not());

        //set the Row Factory of the table
        setRowFactory();

        //Set row selection as default
        setRowSelection();
    }

    private void setCellValueFactories() {
        snCol.setCellValueFactory(new PropertyValueFactory("sn"));
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory("gender"));
        phoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory("address"));
    }

    /**
     * Change the cell selection boolean value of TableView
     */
    public void setRowSelection() {
        tableView.getSelectionModel().clearSelection();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
    }

    /**
     * Change the cell selection boolean value of TableView
     */
    public void setCellSelection() {
        tableView.getSelectionModel().clearSelection();
        tableView.getSelectionModel().setCellSelectionEnabled(true);

    }

    /**
     * Set Row Factory for the TableView
     */
    public void setRowFactory() {
        tableView.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {
            @Override
            public TableRow<Person> call(TableView<Person> p) {
                final TableRow<Person> row = new TableRow<Person>();
                row.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent t) {
                        setSelection(row);
                    }
                });

                row.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        if (rowRadio.isSelected()) {
                            Dragboard db = row.getTableView().startDragAndDrop(TransferMode.COPY);
                            ClipboardContent content = new ClipboardContent();
                            content.put(dataFormat, "XData");
                            db.setContent(content);
                            setSelection(row);
                            t.consume();
                        }
                    }
                });
                return row;
            }
        });
    }

    /**
     * This function helps to make the Cell Factory for specific TableColumn
     *
     * @param col
     */
    public void setCellColumnSelection(final TableColumn col) {
        col.setCellFactory(new Callback<TableColumn<Person, ?>, TableCell<Person, ?>>() {
            @Override
            public TableCell<Person, ?> call(
                      TableColumn<Person, ?> p) {
                final TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.toString());
                        }
                    }
                };

                cell.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent t) {
                        setSelection(cell, col);
                    }
                });

                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        if (cellRadio.isSelected() && colSelect.getValue() == col) {
                            Dragboard db = cell.getTableView().startDragAndDrop(TransferMode.COPY);
                            ClipboardContent content = new ClipboardContent();
                            content.put(dataFormat, "XData");
                            db.setContent(content);
                            setSelection(cell, col);
                            t.consume();
                        }
                    }
                });
                return cell;

            }
        });
    }

    /**
     * For the changes on table cell selection used only on the TableCell selection mode
     *
     * @param cell
     */
    private void setSelection(IndexedCell cell) {
        if (rowRadio.isSelected()) {
            if (cell.isSelected()) {
                System.out.println("False");
                tableView.getSelectionModel().clearSelection(cell.getIndex());
            } else {
                System.out.println("true");
                tableView.getSelectionModel().select(cell.getIndex());
            }
        }

    }

    /**
     * For the changes on the table row selection used only on TableRow selection mode
     *
     * @param cell
     * @param col
     */
    private void setSelection(IndexedCell cell, TableColumn col) {
        if (cellRadio.isSelected()) {
            if (cell.isSelected()) {
                System.out.println("False enter");
                tableView.getSelectionModel().clearSelection(cell.getIndex(), col);
            } else {
                System.out.println("Select");
                tableView.getSelectionModel().select(cell.getIndex(), col);
            }
        }
    }

    /**
     * Provides the Dummy Data for this application in string format
     *
     * @param length
     * @return String
     */
    public String getDummyText(int length) {
        String most = "abdflntiso";
        String alpha = "abcdefghijkmopqrstuvwxyz";
        StringBuffer b = new StringBuffer();
        int chars = 0;
        for (int i = 0; i < length; i++) {
            if (chars > 2 && chars > Math.random() * 10) {
                b.append(" ");
                chars = 0;
                continue;
            }
            if (chars == 0 || i % 2 == 0) {
                b.append(most.charAt((int)(Math.random() * most.length())));
            } else {
                b.append(alpha.charAt((int)(Math.random() * alpha.length())));
            }
            chars++;
        }
        return b.toString();
    }

    /**
     * Provides the dummy String
     *
     * @param length
     * @return
     */
    public String getDummyDigits(int length) {

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append((int)(Math.random() * 9));
        }
        return b.toString();
    }

    /**
     * Provides the dummy Person data.
     *
     * @return
     */
    public ObservableList<Person> getData() {
        String[] names = new String[]{"Narayan", "Phil", "Pablo", "Michael", "Mike", "Timur", "Oszkar", "David"};

        ObservableList<Person> persons = FXCollections.observableArrayList();
        for (int i = 1; i < 500; i++) {
            Person p = new Person();
            p.setSn(i);
            p.setName(names[(int)(Math.random() * names.length)]);
            p.setAddress(getDummyText(15));
            p.setPhone(getDummyDigits(9));
            p.setGender(Gender.values()[i % 3]);
            persons.add(p);
        }
        return persons;
    }
}
