package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AppGUIController implements Initializable {

    @FXML
    private TextField field;
    @FXML
    private TextField field2;
    @FXML
    private TextField field3;
    @FXML
    private TextField field4;
    @FXML
    private TextField field5;
    @FXML
    private TextField field6;
    @FXML
    private TextField field7;
    @FXML
    private TextField field8;

    @FXML
    private Label label;

    private ObservableList<Transaction> transactionsData = FXCollections.observableArrayList();

    @FXML
    private TableView<Transaction> tableTransactions;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, String> agentColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML
    private TableColumn<Transaction, String> tagColumn;
    @FXML
    private TableColumn<Transaction, String> catColumn;

    @FXML
    private TableColumn<Transaction, String> discriptionColumn;

    @FXML
    private TableColumn<Transaction, Float> amountColumn;


    @FXML
    private void handleAction(ActionEvent event) throws IOException, ParseException {
        Table table = new Table();
        String description = field.getText();
        String tag = field2.getText();
        String cat = field3.getText();
        String agent = field4.getText();
        String amount = field5.getText();
        int id = table.freeId();
        Date date = new Date();
        try {
            float am = Float.parseFloat(amount);
            if (am < 0) {
                System.out.println("Некорректный ввод.");
            } else {
                table.addRecord(id, description, date, tag, cat, agent, Float.parseFloat(amount));

                transactionsData.add(table.table[table.getLen() - 1]);
            }
        } catch (NumberFormatException pe) {
            System.out.println("Некорректный ввод.");
        }
    }
    @FXML
    private void handleAction2(ActionEvent event) throws IOException, ParseException {
        Table table = new Table();
        int row = tableTransactions.getSelectionModel().getSelectedIndex();
        table.deleteRecord(table.table[row].getId());
        transactionsData.remove(row);
    }
    @FXML
    private void handleAction4(ActionEvent event) throws IOException, ParseException {
        Table table = new Table();
        String text = field6.getText();
        String start = field7.getText();
        String finish = field8.getText();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
        try {
            if (!start.equals("")) {
                Date s = formatForDateNow.parse(start);
            }
            if (!finish.equals("")) {
                Date f = formatForDateNow.parse(finish);
            }
            table.makeSubTable(text, start, finish);
        } catch (ParseException pe) {
            System.out.println("Некорректный формат даты.");
        }
    }
    @FXML
    private void handleAction3(ActionEvent event) throws IOException, ParseException {
        Table table = new Table();
        String text = field6.getText();
        String start = field7.getText();
        String finish = field8.getText();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
        try {
            if (!start.equals("")) {
                Date s = formatForDateNow.parse(start);
            }
            if (!finish.equals("")) {
                Date f = formatForDateNow.parse(finish);
            }
            System.out.println(table.balance(text, start, finish));
        } catch (ParseException pe) {
            System.out.println("Некорректный формат даты.");
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        try {
            initData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("id"));
        discriptionColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("date"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("tag"));
        catColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("category"));
        agentColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("agent"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Float>("amount"));

        tableTransactions.setItems(transactionsData);
    }

    @FXML
    private void initData() throws IOException, ParseException {
        Date date = new Date();
        Table table = new Table();
        for (int i = 0; i < table.getLen(); ++i) {
            transactionsData.add(table.table[i]);
        }
    }
}
