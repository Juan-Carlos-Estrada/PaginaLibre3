package org.paginalibre3.DAO;

import java.util.List;
import org.paginalibre3.model.Usuario;

public interface UsuarioDAO {

    Usuario autenticar(String username, String passwordHash);

    List<Usuario> listarUsuarios();

    List<Usuario> listarTodos();

    boolean registrarUsuario(String username, String passwordHash, String rol);

    boolean actualizarUsuario(int id, String rol, boolean activo);

    boolean desactivarUsuario(int id);

    boolean cambiarPassword(int id, String passwordActualHash, String passwordNuevoHash);
}