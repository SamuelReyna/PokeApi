/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entrenadoresPokedex")
@IdClass(EntrenadorPokedexId.class)
public class EntrenadoresPokedex {
    
    @Id
    private int idEntrenador;
    
    @Id
    private int idPokedex;
    
       @ManyToOne
    @JoinColumn(name = "identrenador", insertable = false, updatable = false)
    private Entrenador entrenador;

    @ManyToOne
    @JoinColumn(name = "idpokedex", insertable = false, updatable = false)
    private Pokedex pokedex;
}
