package org.paginalibre3.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.paginalibre3.DAO.UsuarioDAO;
import org.paginalibre3.model.Usuario;
import org.paginalibre3.util.Conexion;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario autenticar(String username, String passwordHash) {
        Usuario usuario = null;
        String sql = "{CALL sp_iniciar_sesion(?, ?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_usuarios()}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement stmt = con.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRol(rs.getString("rol"));
                u.setActivo(rs.getBoolean("activo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizarUsuario(int id, String rol, boolean activo) {
        String sql = "{CALL sp_actualizar_usuario(?, ?, ?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, rol);
            stmt.setBoolean(3, activo);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean desactivarUsuario(int id) {
        String sql = "{CALL sp_desactivar_usuario(?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cambiarPassword(int id, String passwordActualHash, String passwordNuevoHash) {
        String sql = "{CALL sp_cambiar_password(?, ?, ?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, passwordActualHash);
            stmt.setString(3, passwordNuevoHash);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario iniciarSesion(String usuario, String passwordHash) {
        return autenticar(usuario, passwordHash);
    }

    @Override
    public boolean registrarUsuario(String usuario, String passwordHash, String rol) {
        String sql = "{CALL sp_registrar_usuario(?, ?, ?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, passwordHash);
            stmt.setString(3, rol);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return listarUsuarios();
    }
}