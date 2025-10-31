package risosu.it.PokeApiClient.ML;

import java.util.List;

public class Pokemon {

    private Long id;

    public Pokemon(Long id, String name, int order, String height, String weight, boolean is_default, List<Sprite> sprites, List<Cries> cries) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.height = height;
        this.weight = weight;
        this.is_default = is_default;
        this.sprites = sprites;
        this.cries = cries;
    }

    public Pokemon() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    private int order; //Pokemons del mismo orden.

    private String height; //Altura del pokemon expresada en decimetros 1dc = 10cm 

    private String weight; //Peso de pokemon expresado en hectogramos 1hectogramo = 100 gramos

    private boolean is_default; //Si el pokemon se encuentra en su forma base (No evolucionado )

    private List<Sprite> sprites; //Imagenes del pokemon 

    private List<Cries> cries; //Efectos de sonido

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
