package risosu.it.PokeApiClient.ML;

import com.fasterxml.jackson.annotation.JsonProperty;

    
public class Rol {
    
    @JsonProperty("idrol")
    private int idrol;

    private String nombre;

    public Rol() {
    }

    public Rol(String Nombre, int IdRol) {
        this.nombre = Nombre;
        this.idrol = IdRol;
    }

    public int getIdRol() {
        return idrol;
    }

    public void setIdRol(int IdRol) {
        this.idrol = IdRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }
}
