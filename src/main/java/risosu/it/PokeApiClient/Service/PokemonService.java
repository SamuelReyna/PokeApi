package risosu.it.PokeApiClient.Service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import risosu.it.PokeApiClient.DAO.IPokemonRepository;
import risosu.it.PokeApiClient.JPA.Pokemon;

@Service
public class PokemonService {

    @Autowired
    private IPokemonRepository iPokemonRepository;

    public int Count() {
        return (int) iPokemonRepository.count();
    }

    public List<Pokemon> GetTypes() {
        List<Pokemon> pokemons = iPokemonRepository.findAll();
        return pokemons;
    }

}
