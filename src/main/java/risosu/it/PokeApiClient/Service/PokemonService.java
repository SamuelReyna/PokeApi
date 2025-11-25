package risosu.it.PokeApiClient.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import risosu.it.PokeApiClient.DAO.IPokemonRepository;
import risosu.it.PokeApiClient.DAO.IPokemonPokedex;
import risosu.it.PokeApiClient.JPA.PokedexPokemon;
import risosu.it.PokeApiClient.JPA.Pokemon;

@Service
public class PokemonService {

    @Autowired
    private IPokemonRepository iPokemonRepository;

    @Autowired
    private IPokemonPokedex pokemonPokedex;

    public int Count() {
        return (int) iPokemonRepository.count();
    }

    public List<Pokemon> GetTypes() {
        List<Pokemon> pokemons = iPokemonRepository.findAll();
        return pokemons;
    }

    public Long CountPokemon(int idPokemon) {
        Long timesSave = pokemonPokedex.countByIdPokemon(idPokemon);
        return timesSave;
    }

    public List<Pokemon> GetFavs() {
        List<Pokemon> poke = pokemonPokedex.findAllPokemons();
        List<Pokemon> pokemons = iPokemonRepository.findAll();
        List<Integer> ids = poke.stream().map(p -> (Integer) p.getIdPokemon()).toList();

        return pokemons.stream()
                .filter(pokemon
                        -> ids.contains(
                        pokemon.getIdPokemon()))
                .toList();
    }

}
