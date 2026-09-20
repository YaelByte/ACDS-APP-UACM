module chinsons.app.uacm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;

    opens chinsons.app.uacm to javafx.fxml;
    exports chinsons.app.uacm;
}
