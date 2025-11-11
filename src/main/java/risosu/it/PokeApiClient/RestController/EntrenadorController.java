package risosu.it.PokeApiClient.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import risosu.it.PokeApiClient.Service.EntrenadorService;
import risosu.it.PokeApiClient.JPA.Entrenador;

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

    @PostMapping()
    public ResponseEntity Add(@RequestBody Entrenador entrenador) {
        return ResponseEntity.ok(entrenadorService.Add(entrenador));
    }

    @PatchMapping("/{idEntrenador}")
    public ResponseEntity Patch(@PathVariable("idEntrenador") Long idEntrenador, @RequestBody Entrenador entrenador) {
        return ResponseEntity.ok(entrenadorService.patchEntrenador(idEntrenador, entrenador));
    }

    @DeleteMapping("/{idEntrenador}")
    public ResponseEntity Delete(@PathVariable("idEntrenador") int idEntrenador) {
        return ResponseEntity.ok(entrenadorService.Delete(Long.valueOf(idEntrenador)));
    }

}
