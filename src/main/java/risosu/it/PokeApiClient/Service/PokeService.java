/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import risosu.it.PokeApiClient.ML.Pokemon;

@Service
public class PokeService {

    public List<Pokemon> getFavTypes() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Pokemon> pokemonList = Arrays.asList(
                    mapper.readValue(new File("pokemons.json"), Pokemon[].class)
            );

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity< List<Map< String, String>>> requestEntity
                    = restTemplate.exchange(
                            "http://localhost:8081/api/pokemon",
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference< List<Map<String, String>>>() {
                    });
            if (requestEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                List<Map<String, String>> pokefavs = requestEntity.getBody();
                System.out.println(pokefavs);

                List<Pokemon> pokefavs2 = pokemonList.stream()
                        .filter(pokemon
                                -> pokefavs.stream()
                                .anyMatch(fav -> fav.get("idJson").equals(String.valueOf(pokemon.getId())))
                        )
                        .collect(Collectors.toList());

                return pokefavs2;

            }

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
        return null;
    }

}
