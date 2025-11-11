package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private int IdRol;
    @Column(name = "nombre")
    private String nombre;

    public int getIdrol() {
        return IdRol;
    }

    public void setIdrol(int idrol) {
        this.IdRol = idrol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol(int idrol, String nombre) {
        this.IdRol = idrol;
        this.nombre = nombre;
    }

    public Rol() {
    }

}
