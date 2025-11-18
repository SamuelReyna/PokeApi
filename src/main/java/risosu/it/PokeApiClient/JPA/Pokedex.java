package risosu.it.PokeApiClient.JPA;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "pokedex")
public class Pokedex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpokedex")
    private int idPokedex;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "pokedex", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "pokedexpokemon")
    private List<PokedexPokemon> pokedexPokemons;

    public List<PokedexPokemon> getPokedexPokemons() {
        return pokedexPokemons;
    }

    public void setPokedexPokemons(List<PokedexPokemon> pokedexPokemons) {
        this.pokedexPokemons = pokedexPokemons;
    }

    public int getIdPokedex() {
        return idPokedex;
    }

    public void setIdPokedex(int idPokedex) {
        this.idPokedex = idPokedex;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
