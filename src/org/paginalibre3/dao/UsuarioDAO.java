package org.paginalibre3.dao;

import org.paginalib3.model.Usuario;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;
import org.paginalibre3.util.Conexion;

public class UsuarioDao {

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
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return usuario;
    }