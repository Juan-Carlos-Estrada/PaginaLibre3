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
import org.paginalibre3.dao.impl.UsuarioDAOImpl;
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
        usuarioDAO = new UsuarioDAOImpl();
        if (lblMensaje != null) lblMensaje.setText("");
        if (lblMensajeRegistro != null) lblMensajeRegistro.setText("");
    }

    @FXML
    public void eventoInicioSesion(ActionEvent evento) {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor, complete todos sus datos.");
            return;
        }

        String passwordHash = SecurityUtil.hashSHA256Password(password);
        Usuario usuarioIniciado = usuarioDAO.autenticar(usuario, passwordHash);

        if (usuarioIniciado != null) {
            lblMensaje.setText("Inicio correcto");
            abrirDashBoard(usuarioIniciado);
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }

    @FXML
    public void handleRegistrar() {
        String usuario = txtNuevoUsuario.getText().trim();
        String password = txtNuevaPassword.getText().trim();

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

        switch (usuario.getRol().toLowerCase()) {
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
                tituloDashboard = "Panel de Administración";
            }
        }

        try {
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent raiz = cargadorFXML.load();

            Stage escenario = (Stage) btnIniciarSesion.getScene().getWindow();
            escenario.setScene(new Scene(raiz));
            escenario.setTitle(tituloDashboard);
            escenario.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + rutaFXML + " -> " + e.getMessage());
            e.printStackTrace();
            lblMensaje.setText("Error al cargar el dashboard");
        }
    }
}