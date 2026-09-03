package org.paginalibre3.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.paginalibre3.dao.UsuarioDao;
import org.paginalibre3.model.Usuario;
import org.paginalibre3.system.Main;
import org.paginalibre3.util.SecurityUtil;

public class UsuariosFXController implements Initializable {

    @FXML
    private TextField txtIdUsuario;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> comboRol;
    @FXML
    private CheckBox chkActivo;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colUsername;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, Boolean> colActivo;

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboRol.setItems(FXCollections.observableArrayList("admin", "bodega", "cajero"));
        configurarTabla();
        cargarTabla();
        seleccionarFila();
        txtIdUsuario.setEditable(false);
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("usrname"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
    }

    private void cargarTabla() {
        listaUsuarios.setAll(usuarioDao.listarTodos());
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void seleccionarFila() {
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtIdUsuario.setText(String.valueOf(newSelection.getId()));
                        txtUsername.setText(newSelection.getUsrname());
                        comboRol.setValue(newSelection.getRol());
                        chkActivo.setSelected(newSelection.isActivo());
                        txtPassword.clear();
                    }
                });
    }

    @FXML
    private void handleGuardar() {
        String passwordHash = SecurityUtil.hashSHA256Password(txtPassword.getText().trim());
        boolean exito = usuarioDao.registrarUsuario(
                txtUsername.getText().trim(), passwordHash, comboRol.getValue());

        if (exito) {
            lblMensaje.setText("Usuario registrado exitosamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarError("No se pudo registrar el usuario (¿username duplicado?).");
        }
    }

    @FXML
    private void handleActualizarRol() {
        int id = Integer.parseInt(txtIdUsuario.getText());
        boolean exito = usuarioDao.actualizarUsuario(id, comboRol.getValue(), chkActivo.isSelected());

        if (exito) {
            lblMensaje.setText("Usuario actualizado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarError("No se pudo actualizar el usuario.");
        }
    }

    @FXML
    private void handleDesactivar() {
        int id = Integer.parseInt(txtIdUsuario.getText());
        boolean exito = usuarioDao.desactivarUsuario(id);

        if (exito) {
            lblMensaje.setText("Usuario desactivado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            mostrarError("No se pudo desactivar el usuario.");
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void handleActualizarTabla() {
        cargarTabla();
        lblMensaje.setText("Tabla actualizada.");
    }

    @FXML
    private void handleVolver() {
        try {
            Main.cambiarVista("/org/paginalibre3/view/MenuPrincipalDashboard.fxml");
        } catch (Exception e) {
            mostrarError("Error al volver al menú: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtIdUsuario.clear();
        txtUsername.clear();
        txtPassword.clear();
        comboRol.setValue(null);
        chkActivo.setSelected(false);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}