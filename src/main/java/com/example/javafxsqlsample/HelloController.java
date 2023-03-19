package com.example.javafxsqlsample;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TableColumn<Users, String> col_email;

    @FXML
    private TableColumn<Users, Integer> col_id;

    @FXML
    private TableColumn<Users, String> col_lastname;

    @FXML
    private TableColumn<Users, String> col_mobile;

    @FXML
    private TableColumn<Users, String> col_firstname;

    @FXML
    private TableView<Users> table_users;

    @FXML
    private TextField text_email;

    @FXML
    private TextField text_lastname;

    @FXML
    private TextField text_firstname;

    @FXML
    private TextField text_mobile;
    @FXML
    private TextField text_id;
    @FXML
    private TextField filterField;

    ObservableList<Users> listM;
    ObservableList<Users> dataList;
    int index = -1;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement pst = null;

    public void addUsers() {

        connection = DataBase.connectDB();
        String sql = "INSERT INTO student ( firstname,lastname,email,mobile,id) values(?,?,?,?,?)";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(5, text_id.getText());


            pst.setString(1, text_firstname.getText());
            pst.setString(2, text_lastname.getText());
            pst.setString(3, text_email.getText());
            pst.setString(4, text_mobile.getText());
            pst.execute();
            //DataBase.alert("Add Success");
            updateTable();
            search_user();
        } catch (Exception e) {
            e.printStackTrace();
            DataBase.alert("id tekrarı");
        }
    }

    @FXML
    void getSelected(MouseEvent event) {
        index = table_users.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        text_id.setText(col_id.getCellData(index).toString());
        text_firstname.setText(col_firstname.getCellData(index).toString());
        text_lastname.setText(col_lastname.getCellData(index).toString());
        text_email.setText(col_email.getCellData(index).toString());
        text_mobile.setText(col_mobile.getCellData(index).toString());

    }

    public void edit() {
        try {
            connection = DataBase.connectDB();
            String value1 = text_id.getText();
            String value2 = text_firstname.getText();
            String value3 = text_lastname.getText();
            String value4 = text_email.getText();
            String value5 = text_mobile.getText();
            String sql = "update student set id= '" + value1 + "',firstname= '" + value2 + "',lastname= '" +
                    value3 + "',email= '" + value4 + "',mobile= '" + value5 + "' where id='" + value1 + "' ";
            pst = connection.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Update");
            updateTable();
            search_user();
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, e);
        }

    }

    public void delete() {
        connection = DataBase.connectDB();
        String sql = "delete from student where id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, text_id.getText());
            pst.execute();
            // JOptionPane.showMessageDialog(null, "Delete");
            updateTable();
            search_user();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
        }

    }


    public void updateTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<Users, Integer>("id"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<Users, String>("firstname"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<Users, String>("lastname"));
        col_email.setCellValueFactory(new PropertyValueFactory<Users, String>("email"));
        col_mobile.setCellValueFactory(new PropertyValueFactory<Users, String>("mobile"));
        listM = DataBase.getDatausers();
        table_users.setItems(listM);
    }

    @FXML
    void search_user() {
        col_id.setCellValueFactory(new PropertyValueFactory<Users, Integer>("id"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<Users, String>("firstname"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<Users, String>("lastname"));
        col_email.setCellValueFactory(new PropertyValueFactory<Users, String>("email"));
        col_mobile.setCellValueFactory(new PropertyValueFactory<Users, String>("mobile"));

        dataList = DataBase.getDatausers();
        table_users.setItems(dataList);
        FilteredList<Users> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches firstname
                } else if (person.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches lastname
                } else if (person.getMobile().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches mobile
                } else if (String.valueOf(person.getEmail()).indexOf(lowerCaseFilter) != -1)
                    return true;// Filter matches email

                else
                    return false; // Does not match.
            });
        });
        SortedList<Users> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_users.comparatorProperty());
        table_users.setItems(sortedData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
        search_user();
    }
}