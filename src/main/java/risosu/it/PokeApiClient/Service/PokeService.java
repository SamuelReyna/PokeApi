package risosu.it.PokeApiClient.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
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
            ResponseEntity<List<Map<String, Object>>> requestEntity
                    = restTemplate.exchange(
                            "http://localhost:8081/api/pokemon/pokedex",
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });
            if (requestEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                List<Map<String, Object>> pokefavs = requestEntity.getBody();

// Crear un mapa: idsJson -> idsPokemon
                Map<Integer, Integer> jsonToPokeMap = pokefavs.stream()
                        .collect(Collectors.toMap(
                                p -> (Integer) p.get("idJson"),
                                p -> (Integer) p.get("idPokemon")
                        ));

                List<Integer> idsJson = pokefavs.stream()
                        .map(p -> (Integer) p.get("idJson"))
                        .toList();

                List<Pokemon> pokefavs2 = pokemonList.stream()
                        .filter(pokemon -> idsJson.contains(pokemon.getId()))
                        .toList();

                pokefavs2.forEach(pokemon -> {
                    try {
                        // Obtener el idPokemon correspondiente al idJson
                        Integer idPokemon = jsonToPokeMap.get(pokemon.getId());

                        if (idPokemon != null) {
                            String url = "http://localhost:8081/api/pokemon/" + idPokemon + "/count";
                            Integer count = restTemplate.getForObject(url, Integer.class);
                            pokemon.setFavorites(count != null ? count : 0);
                        }

                    } catch (Exception e) {
                        System.err.println("Error obteniendo favoritos para pokemon " + pokemon.getId() + ": " + e.getMessage());
                        pokemon.setFavorites(0);
                    }
                });

                return pokefavs2;

            }

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
        return null;
    }

    private List<Pokemon> FavsByEntrenador;

    public List<Pokemon> getFavsByEntrenador() {
        return FavsByEntrenador;
    }

    public void setFavsByEntrenador(List<Pokemon> FavsByEntrenador) {
        this.FavsByEntrenador = FavsByEntrenador;
    }
}
