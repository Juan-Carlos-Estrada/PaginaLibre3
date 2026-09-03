package org.paginalibre3.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent; // Import necesario para manejar ActionEvent
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
import org.paginalibre3.system.Main; // Asegúrate de ajustar esta ruta según la ubicación real de tu clase Main

public class MenuPrincipalDashboardController implements Initializable, DashboardController {

    private Usuario usuarioActual;

    @FXML private Button btnCerrarSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @Override
    public void iniciarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    // --- Métodos de Navegación a Vistas / Tablas ---

    @FXML
    private void handleUsuarios(ActionEvent event) {
        try {
            Main.cambiarVista("/org/paginalibre3/view/UsuariosView.fxml");
        } catch (Exception e) {
            mostrarError("Error al cargar la vista de Usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void handleCambiarPassword() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/org/paginalibre3/view/CambioPasswordView.fxml"));
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

    // --- Métodos de Acción General ---

    @FXML
    private void handleNoDisponible() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Módulo no disponible");
        alert.setHeaderText(null);
        alert.setContentText("Este módulo no está disponible aún.");
        alert.showAndWait();
    }

    @FXML
    private void handleSalir() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/org/paginalibre3/view/InicioSesionView.fxml"));
            Parent raiz = cargador.load();

            Stage escenario = (Stage) btnCerrarSesion.getScene().getWindow();
            escenario.setScene(new Scene(raiz));
            escenario.setTitle("MKAA Librería - Inicio de Sesión");
            escenario.show();
        } catch (IOException e) {
            mostrarError("Error al regresar al Inicio de Sesión: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}