/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.JPA;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entrenadorespokedex")
@IdClass(PokedexEntrenadorId.class)
public class PokedexEntrenador {

    @Id
    @Column(name = "identrenador")
    private int idEntrenador;

    @Id
    @Column(name = "idpokedex")
    private int idPokedex;

    // =================== RELACIÓN CON ENTRENADOR ===================
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "identrenador", insertable = false, updatable = false)
    private Entrenador entrenador;

    // =================== RELACIÓN CON POKEDEX ===================
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "idpokedex", insertable = false, updatable = false)
    private Pokedex pokedex;

    public int getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    public int getIdPokedex() {
        return idPokedex;
    }

    public void setIdPokedex(int idPokedex) {
        this.idPokedex = idPokedex;
    }

}
