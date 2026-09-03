package org.paginalibre3.DAO;
 
import java.util.List;

import org.paginalibre3.model.Usuario;
 
public interface UsuarioDAO {

    Usuario autenticar(String username, String passwordHash);

    List<Usuario> listarUsuarios();

    boolean actualizarUsuario(int id, String rol, boolean activo);

    boolean desactivarUsuario(int id);

    boolean cambiarPassword(int id, String passwordActualHash, String passwordNuevoHash);

    public Usuario iniciarSesion(String usuario, String passwordHash);

    public boolean registrarUsuario(String usuario, String passwordHash, String admin);

}
 