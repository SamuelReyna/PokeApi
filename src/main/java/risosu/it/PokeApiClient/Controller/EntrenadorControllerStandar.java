/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.Controller;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import risosu.it.PokeApiClient.DTO.PokeFavoritoDTO;
import risosu.it.PokeApiClient.DTO.PokemonDTO;

@Controller
@RequestMapping("/pokeControl/standar")
public class EntrenadorControllerStandar {
    
    private final String url = "http://localhost:8081/api/entrenador";
    
    @PostMapping("/favoritos")
    @ResponseBody
    public ResponseEntity<?> actualizarFavorito(@RequestBody PokeFavoritoDTO pokemon,
            HttpSession session) {
        
         String user = (String) session.getAttribute("username");
        //Si se detecta que es el favorito del usuario, se manda al controlador de addFavorites en Backend.
        if(pokemon.getFavorito()){
         
            try {
                RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<?> entity = new HttpEntity<>(pokemon, headers);

        
        HttpEntity<PokeFavoritoDTO> responseEntity = restTemplate.exchange(
                url + "/" + user, 
                HttpMethod.POST, 
                entity, 
                new ParameterizedTypeReference<PokeFavoritoDTO>() {});
            } catch (Exception e) {
                String error = e.getLocalizedMessage();
                System.out.println(error);
            }
        }
        return null;
        
    }
    
}
