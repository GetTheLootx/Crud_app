module com.bigstance.crud_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.bigstance.crud_app to javafx.fxml;
    exports com.bigstance.crud_app;
}