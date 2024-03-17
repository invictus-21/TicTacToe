module com.fx.tictactoejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.fx.tictactoejavafx to javafx.fxml;
    exports com.fx.tictactoejavafx;
}