package org.paginalibre3.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.paginalibre3.model.Usuario;

public class BodegaDashboardController implements Initializable, DashboardController {

    private Usuario usuarioActual;

    @FXML
    private Label lblBienvenida;
    @FXML
    private Button btnCerrarSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void iniciarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        if (lblBienvenida != null) {
            lblBienvenida.setText("Bienvenido, " + usuario.getUsrname());
        }
    }

    @FXML
    private void handleCambiarPassword() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/org/paginalib3/view/CambioPasswordView.fxml"));
            Parent raiz = cargador.load();

            CambioPasswordController controlado = cargador.getController();
            controlado.setUsuario(usuarioActual);

            Stage escenario = (Stage) btnCerrarSesion.getScene().getWindow();
            escenario.setScene(new Scene(raiz));
            escenario.setTitle("Cambiar Contraseña");
            escenario.show();
        } catch (IOException e) {
            mostrarError("Error al cargar Cambiar Contraseña: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalir() {
        try {
            cambiarVista("/org/paginalib3/view/InicioSesionView.fxml", "MKAA Librería - Inicio de Sesión");
        } catch (IOException e) {
            mostrarError("Error al regresar al Inicio de Sesión: " + e.getMessage());
        }
    }

    private void cambiarVista(String rutaFXML, String titulo) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource(rutaFXML));
        Parent raiz = cargador.load();

        Stage escenario = (Stage) btnCerrarSesion.getScene().getWindow();
        escenario.setScene(new Scene(raiz));
        escenario.setTitle(titulo);
        escenario.show();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
