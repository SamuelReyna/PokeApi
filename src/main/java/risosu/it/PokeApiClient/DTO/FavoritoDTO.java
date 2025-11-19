/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.DTO;


public class FavoritoDTO {
 

   private int idPokedex; 
    private int idJson;

    public FavoritoDTO() {}

    public FavoritoDTO(int idPokedex, int idJson) {
        this.idPokedex = idPokedex;
        this.idJson = idJson;
    }

    public int getIdPokedex() {
        return idPokedex;
    }

    public void setIdPokedex(int idPokedex) {
        this.idPokedex = idPokedex;
    }

    public int getIdJson() {
        return idJson;
    }

    public void setIdJson(int idJson) {
        this.idJson = idJson;
    }
}

