package org.paginalibre3.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage escenarioPrincipal;

    @Override
    public void start(Stage stage) {
        escenarioPrincipal = stage;
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("PLTRES");
        stage.setScene(scene);
        stage.show();
    }

    public static void cambiarVista(String fxml) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource(fxml));
        Scene scene = new Scene(root);
        escenarioPrincipal.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}