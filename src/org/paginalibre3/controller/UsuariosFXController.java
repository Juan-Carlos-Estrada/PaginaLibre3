package org.paginalibre3.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.paginalibre3.DAO.UsuarioDAO;
import org.paginalibre3.impl.UsuarioDAOImpl;
import org.paginalibre3.model.Usuario;

public class UsuariosFXController implements Initializable {

    @FXML
    private TableView<Usuario> tblUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colUsername;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, Boolean> colActivo;
    @FXML
    private TextField txtUsername;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnDesactivar;

    private UsuarioDAO usuarioDao;
    private ObservableList<Usuario> listaUsuarios;
    private Usuario usuarioSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Instancia correcta utilizando la implementación real
        usuarioDao = new UsuarioDAOImpl(); 

        cmbRol.getItems().addAll("admin", "bodega", "cajero");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        cargarTablaUsuarios();

        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usuarioSeleccionado = newSelection;
                txtUsername.setText(usuarioSeleccionado.getUsername());
                cmbRol.setValue(usuarioSeleccionado.getRol());
            }
        });
    }

    private void cargarTablaUsuarios() {
        try {
            listaUsuarios = FXCollections.observableArrayList(usuarioDao.listarUsuarios());
            tblUsuarios.setItems(listaUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los usuarios.");
        }
    }

    @FXML
    private void actualizarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor seleccione un usuario de la tabla.");
            return;
        }

        String nuevoRol = cmbRol.getValue();

        if (nuevoRol == null || nuevoRol.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato faltante", "Seleccione un rol válido.");
            return;
        }

        try {
            usuarioSeleccionado.setRol(nuevoRol);
            boolean exito = usuarioDao.actualizarUsuario(usuarioSeleccionado.getId(), nuevoRol, usuarioSeleccionado.isActivo());

            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario actualizado correctamente.");
                cargarTablaUsuarios();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el usuario.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void desactivarUsuario(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor seleccione un usuario de la tabla.");
            return;
        }

        try {
            boolean exito = usuarioDao.desactivarUsuario(usuarioSeleccionado.getId());

            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario desactivado correctamente.");
                cargarTablaUsuarios();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo desactivar el usuario.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        usuarioSeleccionado = null;
        txtUsername.clear();
        cmbRol.setValue(null);
        tblUsuarios.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}