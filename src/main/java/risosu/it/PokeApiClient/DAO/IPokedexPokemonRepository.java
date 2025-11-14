/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package risosu.it.PokeApiClient.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.JPA.PokedexPokemon;

@Repository
public interface IPokedexPokemonRepository extends JpaRepository<PokedexPokemon, Long> {
    List<PokedexPokemon> findByIdPokedex(int idPokedexEntrenador);
}
