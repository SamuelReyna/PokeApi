/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.JPA;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Alien 15
 */
public class PokedexPokemonId implements Serializable {
    private int idPokedex;
    private int idPokemon;
    
    public PokedexPokemonId (){
    }
    
    public PokedexPokemonId (int idPokedex, int idPokemon){
        this.idPokedex = idPokedex;
        this.idPokemon = idPokemon;
    }
    
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokedexPokemonId)) return false;
        PokedexPokemonId that = (PokedexPokemonId) o;
        return Objects.equals(idPokedex, that.idPokedex) &&
               Objects.equals(idPokemon, that.idPokemon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPokedex, idPokemon);
    }


    
}
