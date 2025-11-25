package risosu.it.PokeApiClient.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import risosu.it.PokeApiClient.JPA.PokedexPokemon;
import risosu.it.PokeApiClient.JPA.PokedexPokemonId;
import risosu.it.PokeApiClient.JPA.Pokemon;

@Repository
public interface IPokemonPokedex extends JpaRepository<PokedexPokemon, PokedexPokemonId> {

    long countByIdPokemon(int idPokemon);

    @Query("SELECT pp.pokemon FROM PokedexPokemon pp")
    List<Pokemon> findAllPokemons();
}
