package risosu.it.PokeApiClient.RestController;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import risosu.it.PokeApiClient.DTO.PokeFavoritoDTO;
import risosu.it.PokeApiClient.Service.EntrenadorService;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.JPA.Pokemon;

@RestController
@RequestMapping("api/entrenador")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @GetMapping()
    public ResponseEntity GetAll() {
        return ResponseEntity.ok(entrenadorService.GetAll());
    }

    @GetMapping("/{idEntrenador}")
    public ResponseEntity GetOne(@PathVariable("idEntrenador") Long idEntrenador) {
        return ResponseEntity.ok(entrenadorService.GetById(idEntrenador));
    }

    @GetMapping("/{username}/username")
    public ResponseEntity GetOne(@PathVariable("username") String username) {
        return ResponseEntity.ok(entrenadorService.loadEntrenadorByUsername(username));
    }

    @GetMapping("/count")
    public ResponseEntity Count() {
        return ResponseEntity.ok(entrenadorService.Count());
    }

    @PostMapping()
    public ResponseEntity Add(@RequestBody Entrenador entrenador) {
        return ResponseEntity.ok(entrenadorService.Add(entrenador));
    }

    @PatchMapping("/{idEntrenador}")
    public ResponseEntity Patch(@PathVariable("idEntrenador") Long idEntrenador, @RequestBody Entrenador entrenador) {
        return ResponseEntity.ok(entrenadorService.patchEntrenador(idEntrenador, entrenador));
    }

    @PatchMapping("/{idEntrenador}/estado")
    public ResponseEntity BajaLogica(@PathVariable("idEntrenador") Long idEntrenador) {
        Entrenador entrenador = new Entrenador();
        entrenador.setEstado(1);
        return ResponseEntity.ok(entrenadorService.patchEntrenador(idEntrenador, entrenador));
    }

    @DeleteMapping("/{idEntrenador}")
    public ResponseEntity Delete(@PathVariable("idEntrenador") int idEntrenador) {
        return ResponseEntity.ok(entrenadorService.Delete(Long.valueOf(idEntrenador)));
    }

    @PostMapping("/{user}")
    public ResponseEntity AddFavorites(@RequestBody PokeFavoritoDTO pokemon, @PathVariable String user) {

        Long idPokemon = pokemon.getIdPokemon().longValue();
        Boolean favorito = pokemon.getFavorito();

        return ResponseEntity.ok(entrenadorService.AddFavorites(user, idPokemon, favorito, pokemon));
    }

}
