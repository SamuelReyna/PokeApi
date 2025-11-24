
package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import risosu.it.PokeApiClient.JPA.Pokemon;


public interface IPokemonRepository extends JpaRepository<Pokemon, Long> {
    
     Optional<Pokemon> findByIdJson(int idJson);
         
}
