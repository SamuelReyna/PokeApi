/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.DTO;


public class FavoritosDTO {
 

    private int idPokedex;
    private int idPokemon;

    public FavoritosDTO() {}

    public FavoritosDTO(int idPokedex, int idPokemon) {
        this.idPokedex = idPokedex;
        this.idPokemon = idPokemon;
    }

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
}

