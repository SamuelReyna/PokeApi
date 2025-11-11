package risosu.it.PokeApiClient.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import risosu.it.PokeApiClient.DAO.IEntrenadorRepository;
import risosu.it.PokeApiClient.JPA.Entrenador;

@Service
public class EntrenadorService {

    private final IEntrenadorRepository iEntrenadorRepository;

    private final PasswordEncoder passwordEnconder;

    public EntrenadorService(IEntrenadorRepository iEntrenadorRepository, PasswordEncoder passwordEncoder) {
        this.iEntrenadorRepository = iEntrenadorRepository;
        this.passwordEnconder = passwordEncoder;
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
        entrenador.setPassword(passwordEnconder.encode(entrenador.getPassword()));
        Entrenador newEntrenador = iEntrenadorRepository.save(entrenador);
        return newEntrenador;
    }

    public Entrenador Update(Entrenador entrenador) {
        try {
            // Buscar si existe el entrenador en la BD
            Optional<Entrenador> entrenadorBD = iEntrenadorRepository.findById(Long.valueOf(entrenador.getIdEntrenador()));

            if (entrenadorBD.isEmpty()) {
                // No se encontró el registro, lanzar excepción o manejarlo como null
                throw new RuntimeException("Entrenador no encontrado con ID: " + entrenador.getIdEntrenador());
            }

            // Actualizar los campos necesarios (opcional si el objeto ya viene completo)
            Entrenador updated = iEntrenadorRepository.save(entrenador);

            return updated;

        } catch (Exception e) {
            // Registrar el error en logs (buena práctica)
            System.err.println("❌ Error al actualizar el entrenador: " + e.getMessage());
            e.printStackTrace();

            // Puedes lanzar una excepción personalizada si quieres propagar el error
            throw new RuntimeException("Error al actualizar el entrenador", e);
        }
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
        if (cambios.getEstado() == 1) { // si 0 no es válido como valor de cambio
            entrenador.setEstado(entrenador.getEstado() == 1 ? 0 : 1);
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

    public Entrenador loadByCorreo(String correo) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findByCorreo(correo);
        if (entrenador.isPresent()) {
            return entrenador.get();
        }
        return null;

    }

    public Entrenador Verify(Long idEntrenador) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findById(idEntrenador);
        if (entrenador.isPresent()) {
            entrenador.get().setEstado(1);
            entrenador.get().setVerify(1);
            Entrenador newEntrenador = iEntrenadorRepository.save(entrenador.get());
            return newEntrenador;
        } else {
            return null;
        }
    }
    
}
