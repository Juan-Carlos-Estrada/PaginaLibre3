package org.paginalibre3.model;

public class Usuario {

    private int id;
    private String username;
    private String rol;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(int id, String usrname, String rol) {
        this.id = id;
        this.username = usrname;
        this.rol = rol;
    }

    public Usuario(int id, String username, String rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
