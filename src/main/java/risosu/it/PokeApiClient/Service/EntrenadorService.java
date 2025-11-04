package risosu.it.PokeApiClient.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import risosu.it.PokeApiClient.DAO.IEntrenadorRepository;
import risosu.it.PokeApiClient.JPA.Entrenador;

@Service
public class EntrenadorService {

    private final IEntrenadorRepository iEntrenadorRepository;

    public EntrenadorService(IEntrenadorRepository iEntrenadorRepository) {
        this.iEntrenadorRepository = iEntrenadorRepository;
    }

    public List<Entrenador> GetAll() {
        List<Entrenador> entrenadores = iEntrenadorRepository.findAll();
        return entrenadores;
    }

    public Optional<Entrenador> GetById(Long idEntrenador) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findById(idEntrenador);
        return null;
    }
}
