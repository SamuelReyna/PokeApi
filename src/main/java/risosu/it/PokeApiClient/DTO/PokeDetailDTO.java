package risosu.it.PokeApiClient.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Representa el detalle de un Pokémon obtenido desde la API.
 */
public class PokeDetailDTO {

    private int id;
    private int height; // en decímetros
    private int weight; // en hectogramos
    private int base_experience; // experiencia base
    @JsonProperty("is_default")
    private boolean defaultForm;// si es la forma base o no
    private String sprite; // URL de la imagen
    private String cries;

    private List<String> moves; // lista de movimientos

    public PokeDetailDTO() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBase_experience() {
        return base_experience;
    }

    public void setBase_experience(int base_experience) {
        this.base_experience = base_experience;
    }

    public boolean isDefaultForm() {
        return defaultForm;
    }

    public void setDefaultForm(boolean defaultForm) {
        this.defaultForm = defaultForm;
    }


    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }


    public String getCries() {
        return cries;
    }

    public void setCries(String cries) {
        this.cries = cries;
    }

   

    

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }
}
