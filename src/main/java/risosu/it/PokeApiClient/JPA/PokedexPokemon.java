/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.JPA;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pokedexpokemon")
@IdClass(PokedexPokemonId.class)
public class PokedexPokemon {

    @Id
    @Column(name = "idpokedex")
    private int idPokedex;

    @Id
    @Column(name = "idpokemon")
    private int idPokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpokedex", insertable = false, updatable = false)
    @JsonBackReference(value = "pokedex-pokemons")
    private Pokedex pokedex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpokemon", insertable = false, updatable = false)
    @JsonIgnoreProperties("pokedexes")
    private Pokemon pokemon;

    public int getIdPokedex() {
        return idPokedex;
    }

    public void setIdPokedex(int idPokedex) {
        this.idPokedex = idPokedex;
    }

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public Pokedex getPokedex() {
        return pokedex;
    }

    public void setPokedex(Pokedex pokedex) {
        this.pokedex = pokedex;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }
}
