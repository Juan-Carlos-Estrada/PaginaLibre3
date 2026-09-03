package org.paginalibre3.model;
 
public class Usuario {

    private int id;

    private String username;

    private String rol;

    private boolean activo;
 
    public Usuario() {}
 
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }

    public void setRol(String rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }

    public void setActivo(boolean activo) { this.activo = activo; }

    public String getUsrname() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
 
