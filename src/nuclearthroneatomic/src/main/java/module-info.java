module com.example.nuclearthroneatomic {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.nuclearthroneatomic to javafx.fxml;
    exports com.example.nuclearthroneatomic;
}