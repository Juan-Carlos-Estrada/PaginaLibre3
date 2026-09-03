package org.paginalibre3.util;
 
import org.paginalibre3.model.Usuario;
 
public class SesionGlobal {

    private static Usuario usuarioLogueado;
 
    public static void setUsuarioLogueado(Usuario usuario) {

        usuarioLogueado = usuario;

    }
 
    public static Usuario getUsuarioLogueado() {

        return usuarioLogueado;

    }
 
    public static void cerrarSesion() {

        usuarioLogueado = null;

    }

}
 