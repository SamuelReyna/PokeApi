/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.ML;

import java.util.List;

/**
 *
 * @author Alien 15
 */
public class Pokemon {
    
    private String  name;
    
    private int order; //Pokemons del mismo orden.
    
    private String height; //Altura del pokemon expresada en decimetros 1dc = 10cm 
    
    private String weight; //Peso de pokemon expresado en hectogramos 1hectogramo = 100 gramos
    
    private boolean is_default; //Si el pokemon se encuentra en su forma base (No evolucionado )

    private List<Sprite> sprites ; //Imagenes del pokemon 
 
    private List<Cries> cries ; //Efectos de sonido
    
    public Pokemon(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(List<Sprite> sprites) {
        this.sprites = sprites;
    }

    public List<Cries> getCries() {
        return cries;
    }

    public void setCries(List<Cries> cries) {
        this.cries = cries;
    }
   
    
}
