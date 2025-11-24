
package risosu.it.PokeApiClient.DAO;

import risosu.it.PokeApiClient.JPA.PokedexPokemon;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IPokemonPokedex  extends JpaRepository<PokedexPokemon, Long>{
    
    long countByIdPokemon(int idPokemon);
    
}
