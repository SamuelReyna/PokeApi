/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.ML;

import java.util.List;

/**
 *
 * @author a
 */
public class PokedexResponse {

    public Pokedex pokedex;

    public static class Pokedex {

        public List<PokedexPokemon> pokedexPokemons;
    }

    public static class PokedexPokemon {

        public Pokemon pokemon;
    }

}
