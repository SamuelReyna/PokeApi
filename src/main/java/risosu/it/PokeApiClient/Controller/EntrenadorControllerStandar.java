/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.Controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import risosu.it.PokeApiClient.DTO.FavoritoDTO;
import risosu.it.PokeApiClient.DTO.FavoritosDTO;
import risosu.it.PokeApiClient.DTO.PokeFavoritoDTO;
import risosu.it.PokeApiClient.Service.PokeService;

@Controller
@RequestMapping("/pokeControl/standar")
public class EntrenadorControllerStandar {

    private final String url = "http://localhost:8081/api/entrenador";

    @Autowired
    private PokeService pokeService;
    @PostMapping("/favoritos")
    @ResponseBody
    public ResponseEntity<?> actualizarFavorito(@RequestBody PokeFavoritoDTO pokemon,
            HttpSession session) {

        String user = (String) session.getAttribute("username");

        //Si el usuario elejio añadirlo como favorito:
        if (pokemon.getFavorito()) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<?> entity = new HttpEntity<>(pokemon, headers);

                ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                        url + "/" + user,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<Boolean>() {
                }
                );

                return ResponseEntity.ok(responseEntity.getBody());

            } catch (Exception e) {
                System.out.println("Error al actualizar favorito: " + e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar favorito");
            }
        } else {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<?> entity = new HttpEntity<>(pokemon, headers);

                ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                        url + "/delete/" + user,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<Boolean>() {
                }
                );

                return ResponseEntity.ok(responseEntity.getBody());

            } catch (Exception e) {
                System.out.println("Error al eliminar favorito: " + e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar favorito");
            }

        }

    }

    @GetMapping("/getFavoritos")
    public ResponseEntity<?> GetFavorito(HttpSession session) {
        String user = (String) session.getAttribute("username");
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<List<FavoritoDTO>> responseEntity = restTemplate.exchange(
                    url + "/getFavorites/" + user,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<FavoritoDTO>>() {
            }
            );

            return ResponseEntity.ok(responseEntity.getBody());

        } catch (Exception e) {
            System.out.println("Error al actualizar favorito: " + e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar favorito");
        }
    }

}
