package org.paginalibre3.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.paginalibre3.DAO.UsuarioDAO;
import org.paginalibre3.impl.UsuarioDAOImpl;
import org.paginalibre3.model.Usuario;
import org.paginalibre3.util.SecurityUtil;

public class InicioSesionController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnIniciarSesion;
    @FXML private Label lblMensaje;

    @FXML private TextField txtNuevoUsuario;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private Label lblMensajeRegistro;

    private UsuarioDAO usuarioDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAO();
        if (lblMensaje != null) lblMensaje.setText("");
        if (lblMensajeRegistro != null) lblMensajeRegistro.setText("");
    }

    @FXML
    public void eventoInicioSesion(ActionEvent evento) {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos sus datos.");
            return;
        }

        String passwordHash = SecurityUtil.hashSHA256Password(password);
        Usuario usuarioIniciado = usuarioDAO.iniciarSesion(usuario, passwordHash);

        if (usuarioIniciado != null) {
            lblMensaje.setText("Inicio correcto");
            abrirDashBoard(usuarioIniciado);
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }

    @FXML
    public void handleRegistrar() {
        String usuario = txtNuevoUsuario.getText();
        String password = txtNuevaPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensajeRegistro.setText("Por favor complete todos los campos.");
            return;
        }

        String passwordHash = SecurityUtil.hashSHA256Password(password);
        boolean registrado = usuarioDAO.registrarUsuario(usuario, passwordHash, "admin");

        if (registrado) {
            lblMensajeRegistro.setText("¡Usuario registrado con éxito!");
            txtNuevoUsuario.clear();
            txtNuevaPassword.clear();
        } else {
            lblMensajeRegistro.setText("Error al registrar el usuario.");
        }
    }

    @FXML
    public void handleRegistrarUsuario() {
        handleRegistrar();
    }



    private void abrirDashBoard(Usuario usuario) {
        String rutaFXML;
        String tituloDashboard;

        switch (usuario.getRol()) {
            case "bodega" -> {
                rutaFXML = "/org/paginalibre3/view/BodegaDashboard.fxml";
                tituloDashboard = "Panel de Bodega";
            }
            case "cajero" -> {
                rutaFXML = "/org/paginalibre3/view/CajeroDashboard.fxml";
                tituloDashboard = "Panel de Caja";
            }
            default -> {
                rutaFXML = "/org/paginalibre3/view/MenuPrincipalDashboard.fxml";
                tituloDashboard = "Panel de Administracion";
            }
        }

        try {
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent raiz = cargadorFXML.load();

            Object controlado = cargadorFXML.getController();
            if (controlado instanceof DashboardController dashboard) {
                dashboard.iniciarUsuario(usuario);
            }

            Stage escenario = (Stage) btnIniciarSesion.getScene().getWindow();
            escenario.setScene(new Scene(raiz));
            escenario.setTitle(tituloDashboard);
            escenario.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + rutaFXML + " -> " + e.getMessage());
            e.printStackTrace();
            lblMensaje.setText("Error interno");
        }
    }
}
