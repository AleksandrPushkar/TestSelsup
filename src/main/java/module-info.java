module com.testselsup.testselsup {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.testselsup.testselsup to javafx.fxml;
    exports com.testselsup.testselsup;
}