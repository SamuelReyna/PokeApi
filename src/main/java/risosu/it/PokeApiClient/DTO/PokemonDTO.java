/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.DTO;

/**
 *
 * @author wiccs
 */
public class PokemonDTO {
    private String name;
    private String url;
    private PokeDetailDTO detail;
    
    public PokemonDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PokeDetailDTO getDetail() {
        return detail;
    }

    public void setDetail(PokeDetailDTO detail) {
        this.detail = detail;
    }
    
    
}
