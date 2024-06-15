module org.projetoexecutavel.projetoautorizacaopagamento {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.projetoexecutavel.projetoautorizacaopagamento to javafx.fxml;
    exports org.projetoexecutavel.projetoautorizacaopagamento;
}