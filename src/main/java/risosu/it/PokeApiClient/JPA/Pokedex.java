
package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pokedex")
public class Pokedex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpokedex")
    private int idPokedex;
    
    @Column(name = "nombre")
    private String nombre;
    
}
