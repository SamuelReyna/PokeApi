package risosu.it.PokeApiClient.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (entrenador.isPresent()) {
            return Optional.of(entrenador.get());
        } else {
            return Optional.empty();
        }
    }

    public Entrenador Add(Entrenador entrenador) {
        Entrenador newEntrenador = iEntrenadorRepository.save(entrenador);
        return newEntrenador;
    }

    public Entrenador Update(Entrenador entrenador) {
        Optional<Entrenador> entrenadorBD = iEntrenadorRepository.findById(Long.valueOf(entrenador.getIdEntrenador()));
        Entrenador modifiedEntrenador = new Entrenador();
        if (entrenadorBD.isPresent()) {
            modifiedEntrenador = iEntrenadorRepository.save(entrenador);
        }

        return modifiedEntrenador;
    }

    public Entrenador patchEntrenador(Long id, Entrenador cambios) {
        Optional<Entrenador> optional = iEntrenadorRepository.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("Entrenador no encontrado con ID: " + id);
        }

        Entrenador entrenador = optional.get();

        // Actualizamos solo los campos que vienen no nulos
        if (cambios.getNombre() != null && !cambios.getNombre().isEmpty()) {
            entrenador.setNombre(cambios.getNombre());
        }
        if (cambios.getApellidoPaterno() != null && !cambios.getApellidoPaterno().isEmpty()) {
            entrenador.setApellidoPaterno(cambios.getApellidoPaterno());
        }
        if (cambios.getApellidoMaterno() != null && !cambios.getApellidoMaterno().isEmpty()) {
            entrenador.setApellidoMaterno(cambios.getApellidoMaterno());
        }
        if (cambios.getSexo() != null && !cambios.getSexo().isEmpty()) {
            entrenador.setSexo(cambios.getSexo());
        }
        if (cambios.getCorreo() != null && !cambios.getCorreo().isEmpty()) {
            entrenador.setCorreo(cambios.getCorreo());
        }
        if (cambios.getPassword() != null && !cambios.getPassword().isEmpty()) {
            entrenador.setPassword(cambios.getPassword());
        }
        if (cambios.getRol() != null) {
            entrenador.setRol(cambios.getRol());
        }
        if (cambios.getEstado() != 0) { // si 0 no es válido como valor de cambio
            entrenador.setEstado(cambios.getEstado());
        }

        return iEntrenadorRepository.save(entrenador);
    }

    public Optional<Entrenador> Delete(Long idEntrenador) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findById(idEntrenador);
        iEntrenadorRepository.delete(entrenador.get());
        if (entrenador.isPresent()) {
            return Optional.of(entrenador.get());
        } else {
            return Optional.empty();
        }
    }

    public UserDetails loadEntrenadorByUsername(String username) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findByUsername(username);
        if (entrenador.isPresent()) {
            return (UserDetails) entrenador.get();
        } else {
            return null;
        }

    }

}
