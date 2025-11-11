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
public class EntrenadorPokedexId implements Serializable{
    private int idEntrenador;
    
    private int idPokedex;
    
    public EntrenadorPokedexId (){}
    
    public EntrenadorPokedexId (int idEntrenador, int idPokedex){
        this.idEntrenador = idEntrenador;
        this.idPokedex = idPokedex;  
    }
    
      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokedexPokemonId)) return false;
        EntrenadorPokedexId that = (EntrenadorPokedexId) o;
        return Objects.equals(idEntrenador, that.idEntrenador) &&
               Objects.equals(idPokedex, that.idPokedex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntrenador, idPokedex);
    }
    
}
