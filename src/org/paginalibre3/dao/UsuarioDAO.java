package org.paginalib3.dao;

import org.paginalib3.model.Usuario;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;
import org.paginalib3.util.Conexion;

public class UsuarioDao {

    public Usuario iniciarSesion(String username, String passwordHash) {
        Usuario usuario = null;
        String sql = "{call sp_iniciar_sesion(?, ?)}";

        try(Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consultaCall = conexion.prepareCall(sql)) {

            consultaCall.setString(1, username);
            consultaCall.setString(2, passwordHash);

            try(ResultSet tablaResultado = consultaCall.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    usuario.setId(tablaResultado.getInt(1));
                    usuario.setUsrname(tablaResultado.getString(2));
                    usuario.setRol(tablaResultado.getString(3));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en iniciar sesion: " + e.getMessage());
        }

        return usuario;
    }

    public boolean registrarUsuario(String username, String password, String rol) {
        boolean registroExitoso = false;
        String sql = "{call sp_registrar_usuario(?, ?, ?)}";

        try(Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consultaCall = conexion.prepareCall(sql)) {

            consultaCall.setString(1, username);
            consultaCall.setString(2, password);
            consultaCall.setString(3, rol);

            int filasAfectadas = consultaCall.executeUpdate();
            if (filasAfectadas > 0) {
                registroExitoso = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }

        return registroExitoso;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "{call sp_listar_usuarios()}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(sql);
             ResultSet tablaResultado = consultaCall.executeQuery()) {

            while (tablaResultado.next()) {
                listaUsuarios.add(new Usuario(
                        tablaResultado.getInt("id"),
                        tablaResultado.getString("username"),
                        tablaResultado.getString("rol"),
                        tablaResultado.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }

        return listaUsuarios;
    }

    public Usuario buscarPorId(int id) {
        Usuario usuario = null;
        String sql = "{call sp_buscar_usuario(?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(sql)) {

            consultaCall.setInt(1, id);

            try (ResultSet rs = consultaCall.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("rol"),
                            rs.getBoolean("activo")
                    );
                }
            }