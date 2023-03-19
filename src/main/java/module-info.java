module com.example.javafxsqlsample {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.javafxsqlsample to javafx.fxml;
    exports com.example.javafxsqlsample;
}