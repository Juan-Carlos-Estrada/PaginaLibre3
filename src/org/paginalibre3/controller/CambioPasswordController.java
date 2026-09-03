package org.paginalibre3.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.paginalibre3.dao.UsuarioDAO;
import org.paginalibre3.model.Usuario;
import org.paginalibre3.util.SecurityUtil;

public class CambioPasswordController implements Initializable {

    @FXML
    private PasswordField txtPasswordActual;
    @FXML
    private PasswordField txtPasswordNueva;
    @FXML
    private PasswordField txtPasswordConfirmar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDao = new UsuarioDAO();
    private Usuario usuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblMensaje.setText("");
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @FXML
    private void handleGuardar() {
        String actual = txtPasswordActual.getText();
        String nueva = txtPasswordNueva.getText();
        String confirmar = txtPasswordConfirmar.getText();

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            lblMensaje.setText("Todos los campos son obligatorios.");
            return;
        }
        if (!nueva.equals(confirmar)) {
            lblMensaje.setText("La confirmación no coincide con la nueva contraseña.");
            return;
        }
        if (nueva.length() < 6) {
            lblMensaje.setText("La nueva contraseña debe tener al menos 6 caracteres.");
            return;
        }

        String hashActual = SecurityUtil.hashSHA256Password(actual);
        String hashNuevo = SecurityUtil.hashSHA256Password(nueva);

        boolean exito = usuarioDao.cambiarPassword(usuarioActual.getId(), hashActual, hashNuevo);

        if (exito) {
            lblMensaje.setText("Contraseña actualizada correctamente.");
        } else {
            lblMensaje.setText("La contraseña actual es incorrecta.");
        }
    }

    @FXML
    private void handleCancelar() {
        String rutaFXML;
        switch (usuarioActual.getRol()) {
            case "bodega" -> rutaFXML = "/org/paginalibre3/view/BodegaDashboard.fxml";
            case "cajero" -> rutaFXML = "/org/paginalibre3/view/CajeroDashboard.fxml";
            default -> rutaFXML = "/org/paginalibre3/view/MenuPrincipalDashboard.fxml";
        }

        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent raiz = cargador.load();

            Object controlado = cargador.getController();
            if (controlado instanceof DashboardController dashboard) {
                dashboard.iniciarUsuario(usuarioActual);
            }

            Stage escenario = (Stage) btnGuardar.getScene().getWindow();
            escenario.setScene(new Scene(raiz));
            escenario.show();
        } catch (IOException e) {
            lblMensaje.setText("Error al volver al dashboard: " + e.getMessage());
        }
    }
}
