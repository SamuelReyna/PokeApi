package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import risosu.it.PokeApiClient.JPA.Entrenador;

public interface IEntrenadorRepository extends JpaRepository<Entrenador, Long> {

    Optional<Entrenador> findByUsername(String username);

    Optional<Entrenador> findByCorreo(String correo);

}
