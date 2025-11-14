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
public class PokedexEntrenadorId implements Serializable{
    private int idEntrenador;
    
    private int idPokedex;
    
    public PokedexEntrenadorId (){}
    
    public PokedexEntrenadorId (int idEntrenador, int idPokedex){
        this.idEntrenador = idEntrenador;
        this.idPokedex = idPokedex;  
    }
    
      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokedexEntrenadorId)) return false;
        PokedexEntrenadorId that = (PokedexEntrenadorId) o;
        return Objects.equals(idEntrenador, that.idEntrenador) &&
               Objects.equals(idPokedex, that.idPokedex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntrenador, idPokedex);
    }
    
}
