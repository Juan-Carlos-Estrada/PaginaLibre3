package org.paginalibre3.system;
 
import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.stage.Stage;
 
public class Main extends Application {
 
    private Stage escenarioPrincipal;

    private static Main instancia;
 
    public Main() {

        instancia = this;

    }
 
    public static Main getInstancia() {

        return instancia;

    }
 
    @Override

    public void start(Stage primaryStage) {

        this.escenarioPrincipal = primaryStage;

        this.escenarioPrincipal.setTitle("Sistema de Gestión Librería - Página Viva");

        mostrarInicioSesion();

    }
 
    public void mostrarInicioSesion() {

        cambiarEscena("/org/paginalibre3/view/InicioSesionView.fxml", "Iniciar Sesión - Página Viva");

    }
 
    public void cambiarEscena(String fxmlRuta, String titulo) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRuta));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            escenarioPrincipal.setTitle(titulo);

            escenarioPrincipal.setScene(scene);

            escenarioPrincipal.setResizable(false);

            escenarioPrincipal.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
 
    // Método estático que tus controladores están llamando

    public static void cambiarVista(String fxmlRuta) {

        if (instancia != null) {

            instancia.cambiarEscena(fxmlRuta, "Sistema de Gestión Librería - Página Viva");

        }

    }
 
    public static void main(String[] args) {

        launch(args);

    }

}
 