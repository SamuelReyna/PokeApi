/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package risosu.it.PokeApiClient.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import risosu.it.PokeApiClient.JPA.Pokedex;

@Repository
public interface IPokedexRepository extends JpaRepository<Pokedex, Long>{
    
    
}
