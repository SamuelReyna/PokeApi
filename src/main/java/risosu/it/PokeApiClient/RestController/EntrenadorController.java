package risosu.it.PokeApiClient.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import risosu.it.PokeApiClient.Service.EntrenadorService;

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
}
