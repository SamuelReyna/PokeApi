package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import risosu.it.PokeApiClient.JPA.Entrenador;

@Repository
public interface IEntrenadorRepository extends JpaRepository<Entrenador, Long> {

    Optional<Entrenador> findByUsername(String username);

    Optional<Entrenador> findByCorreo(String correo);

}
