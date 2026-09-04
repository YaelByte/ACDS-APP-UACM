module chinsons.app.uacm {
    requires javafx.controls;
    requires javafx.fxml;

    opens chinsons.app.uacm to javafx.fxml;
    exports chinsons.app.uacm;
}
