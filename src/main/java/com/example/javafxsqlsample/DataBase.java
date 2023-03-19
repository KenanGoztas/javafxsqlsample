package com.example.javafxsqlsample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;


public class DataBase {
    Connection connection=null;

    public static Connection connectDB() {
        try {
            // Class.forName("com.example.javafxsqlsample.Users");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/students", "root", "1f2g3ğ4ı");
            Statement statement = connection.createStatement();
            //alert("Connection established");
//         ResultSet resultSet = statement.executeQuery("select *  from student");
//           while (resultSet.next()) {
//               System.out.println(resultSet.getString("username") + " " + resultSet.getString("email"));            }
           return connection;
        } catch (Exception e) {
            System.out.println();
            return null;

        }

    }

    public static ObservableList<Users> getDatausers() {
        Connection connection = connectDB();
        ObservableList<Users> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM students.student");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                list.add(new Users(Integer.parseInt(resultSet.getString("id")),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("mobile")));

            }

        } catch (Exception e) {

        }
        return list;
    }

    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
