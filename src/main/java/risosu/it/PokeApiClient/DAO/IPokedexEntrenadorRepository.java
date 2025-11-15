/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package risosu.it.PokeApiClient.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.JPA.PokedexEntrenador;

@Repository
public interface IPokedexEntrenadorRepository extends JpaRepository<PokedexEntrenador, Long> {

    @Query("""
        SELECT pe 
        FROM PokedexEntrenador pe
        JOIN FETCH pe.entrenador e
        JOIN FETCH pe.pokedex p
        JOIN FETCH p.pokedexPokemons pp
        JOIN FETCH pp.pokemon po
        WHERE pe.idEntrenador = :idEntrenador
    """)
    List<PokedexEntrenador> cargaPokedexCompleta(@Param("idEntrenador") int idEntrenaador);
    
    List<PokedexEntrenador> findByIdEntrenador(int idEntrenador);
}
