package risosu.it.PokeApiClient.RestController;

import org.springframework.http.ResponseEntity;
import risosu.it.PokeApiClient.Service.PokemonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/pokemon")
public class PokemonController {

    public PokemonController(risosu.it.PokeApiClient.Service.PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    private final PokemonService pokemonService;

    @GetMapping("/totals")
    public ResponseEntity Count() {
        return ResponseEntity.ok(pokemonService.Count());
    }

    @GetMapping()
    public ResponseEntity GetAll() {
        return ResponseEntity.ok(pokemonService.GetTypes());
    }

    @GetMapping("/{idPokemon}/count")
    public ResponseEntity SaveTimes(@PathVariable("idPokemon") int idPokemon) {
        return ResponseEntity.ok(pokemonService.CountPokemon(idPokemon));
    }

    @GetMapping("/pokedex")
    public ResponseEntity pokedex() {
        return ResponseEntity.ok(pokemonService.GetFavs());
    }

}
