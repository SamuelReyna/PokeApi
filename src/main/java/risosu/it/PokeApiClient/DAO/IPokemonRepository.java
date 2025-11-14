/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import risosu.it.PokeApiClient.JPA.Pokemon;

/**
 *
 * @author Alien 15
 */
public interface IPokemonRepository extends JpaRepository<Pokemon, Long> {
    
     Optional<Pokemon> findByIdJson(int idJson);
    
}
