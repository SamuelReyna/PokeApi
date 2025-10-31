package risosu.it.PokeApiClient.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import risosu.it.PokeApiClient.Service.GetAllPokemonsService;

@Controller
public class pokemonController {

    private final GetAllPokemonsService getAllPokemonsService;

    public pokemonController(GetAllPokemonsService getAllPokemonsService) {
        this.getAllPokemonsService = getAllPokemonsService;
    }

    @GetMapping("/pokemon")
    public String GetPokemones() throws Exception {
        getAllPokemonsService.persistirPokemones();
        return "loading";
    }
}
