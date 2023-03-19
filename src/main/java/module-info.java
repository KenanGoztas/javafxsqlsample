module com.example.javafxsqlsample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javafxsqlsample to javafx.fxml;
    exports com.example.javafxsqlsample;
}