/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.DTO;

/**
 *
 * @author digis
 */
public class EntrenadorDTO {

    private Long IdEntrenador;
    private String Nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Sexo;
    private String Correo;
    private String Username;
    private String Password;
    private int Estado;
    private int Verify;

    public int getVerify() {
        return Verify;
    }

    public void setVerify(int Verify) {
        this.Verify = Verify;
    }
    public Rol rol = new Rol();

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public EntrenadorDTO(Rol rol) {
        this.rol = rol;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public EntrenadorDTO() {
    }

    public EntrenadorDTO(Long IdEntrenador, String Nombre, String ApellidoPaterno, String ApellidoMaterno, String Sexo, String Correo, String Username, Rol rol, String Password, int Verify) {
        this.IdEntrenador = IdEntrenador;
        this.Nombre = Nombre;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.Sexo = Sexo;
        this.Correo = Correo;
        this.Username = Username;
        this.Password = Password;
        this.Verify = Verify;
        this.rol = rol;
    }

    public Long getIdEntrenador() {
        return IdEntrenador;
    }

    public void setIdEntrenador(Long IdEntrenador) {
        this.IdEntrenador = IdEntrenador;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

}
