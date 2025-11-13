package risosu.it.PokeApiClient.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import risosu.it.PokeApiClient.ML.Cries;
import risosu.it.PokeApiClient.ML.Habitat;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.PokemonListResponse;
import risosu.it.PokeApiClient.ML.Result;
import risosu.it.PokeApiClient.ML.Sprite;
import risosu.it.PokeApiClient.ML.Stats;
import risosu.it.PokeApiClient.ML.Type;
import risosu.it.PokeApiClient.ML.Species;
import risosu.it.PokeApiClient.ML.SpeciesDetail;
import risosu.it.PokeApiClient.ML.Color;

@Service
public class GetAllPokemonsService {

    private final WebClient webClient;
    private final Semaphore rateLimiter = new Semaphore(10);
    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    private final ObjectMapper mapper = new ObjectMapper();

    public GetAllPokemonsService(WebClient webClient) {
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(() -> rateLimiter.release(10 - rateLimiter.availablePermits()),
                        0, 1, TimeUnit.SECONDS);
        this.webClient = webClient;
    }

    public List<Result> ObtenerListPokemones() {
        var resp = webClient.get().uri("/pokemon?limit=2000")
                .retrieve().bodyToMono(PokemonListResponse.class).block();

        return resp.getResults();

    }

    public void persistirPokemones() throws IOException {
        List<Result> listaPokemones = ObtenerListPokemones();
        List<CompletableFuture<Pokemon>> pokemones = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();

        for (Result r : listaPokemones) {
            CompletableFuture<Pokemon> fut = CompletableFuture.supplyAsync(() -> {
                try {
                    rateLimiter.acquire(); // límite de peticiones
                    return obtenerDetallesPokemon(r.getUrl());
                } catch (InterruptedException e) {
                    System.err.println("Error al obtener " + r.getName() + ": " + e.getMessage());
                    return null;
                } catch (JsonProcessingException ex) {
                    Logger.getLogger(GetAllPokemonsService.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }, executor).whenComplete((p, ex) -> {
                int done = counter.incrementAndGet();
                if (done % 50 == 0) {
                    System.out.println("Procesados " + done + "/" + listaPokemones.size());
                }
            });

            pokemones.add(fut);
        }

        // Esperar a que todos terminen
        List<Pokemon> pokemons = pokemones.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        // Guardar en archivo JSON solo una vez
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("pokemons.json"), pokemons);

        System.out.println("✅ Archivo 'pokemons.json' creado con " + pokemons.size() + " pokemones");
    }

    private Pokemon
            obtenerDetallesPokemon(String url) throws JsonProcessingException {
        var resp = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class
                )
                .block();

        if (resp == null) {
            return null;
        }

        Pokemon pokemon = new Pokemon();
        Cries cries = new Cries();
        Sprite sprites = new Sprite();
        Species speciesML = new Species();

        Map<String, Object> criesMap = (Map<String, Object>) resp.get("cries");
        if (criesMap != null) {
            cries.setLatest((String) criesMap.get("latest"));
            cries.setLegacy((String) criesMap.get("legacy"));
        }
        Map<String, Object> spriteMap = (Map<String, Object>) resp.get("sprites");
        if (spriteMap != null) {
            sprites.setBack_default((String) spriteMap.get("back_default"));
            sprites.setBack_female((String) spriteMap.get("back_female"));
            sprites.setBack_shiny((String) spriteMap.get("back_shiny"));
            sprites.setBack_shiny_female((String) spriteMap.get("back_shiny_female"));
            sprites.setFront_default((String) spriteMap.get("front_default"));
            sprites.setFront_female((String) spriteMap.get("front_female"));
            sprites.setFront_shiny((String) spriteMap.get("front_shiny"));
            sprites.setFront_shiny_female((String) spriteMap.get("front_shiny_female"));
        }

        List<Map<String, Object>> statsList = (List<Map<String, Object>>) resp.get("stats");
        List<Stats> listaStats = new ArrayList<>();
        if (statsList != null) {
            for (Map<String, Object> s : statsList) {
                Stats stat = new Stats();

                stat.setBase_stat((Integer) s.get("base_stat"));
                stat.setEffort((Integer) s.get("effort"));

                // el campo "stat" dentro de cada elemento es otro objeto (Map)
                Map<String, Object> statInfo = (Map<String, Object>) s.get("stat");
                if (statInfo != null) {
                    stat.setName((String) statInfo.get("name"));
                    stat.setUrl((String) statInfo.get("url"));
                }

                listaStats.add(stat);
            }
        }
        List<Map<String, Object>> typesList = (List<Map<String, Object>>) resp.get("types");
        List<Type> listTypes = new ArrayList<>();

        if (typesList != null) {
            for (Map<String, Object> t : typesList) {
                Type type = new Type();
                type.setSlot((Integer) t.get("slot"));
                Map<String, Object> typeInfo = (Map<String, Object>) t.get("type");
                if (typeInfo != null) {
                    type.setName((String) typeInfo.get("name"));
                    type.setUrl((String) typeInfo.get("url"));
                }
                listTypes.add(type);
            }
        }

        Map<String, Object> species = (Map<String, Object>) resp.get("species");

        if (species != null) {

            ObjectMapper mapper = new ObjectMapper();
            speciesML.setName((String) species.get("name"));
            speciesML.setUrl((String) species.get("url"));
            String specieJson = webClient
                    .get()
                    .uri(speciesML.getUrl())
                    .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                    .retrieve()
                    .bodyToMono(String.class).block();

            Map<String, Object> specieResp = mapper.readValue(specieJson, Map.class);
            SpeciesDetail speciesDetail = new SpeciesDetail();
            speciesDetail.setId((int) specieResp.get("id"));
            speciesDetail.setName((String) specieResp.get("name"));

            Map<String, Object> habitats = (Map<String, Object>) specieResp.get("habitat");
            if (habitats != null) {
                Habitat habitat = new Habitat();
                habitat.setName((String) habitats.get("name"));
                habitat.setUrl((String) habitats.get("url"));
                speciesDetail.setHabitat(habitat);
            }

            Map<String, Object> colors = (Map<String, Object>) specieResp.get("color");
            if (colors != null) {
                Color color = new Color();
                color.setName((String) colors.get("name"));
                color.setUrl((String) colors.get("url"));
                speciesDetail.setColor(color);
            }

            speciesML.setSpeciesDetail(speciesDetail);
        }

        pokemon.setId((Integer) resp.get("id"));
        pokemon.setName((String) resp.get("name"));
        pokemon.setHeight((Integer) resp.get("height"));
        pokemon.setWeight((Integer) resp.get("weight"));
        pokemon.setOrder((Integer) resp.get("order"));
        pokemon.setIs_default((boolean) resp.get("is_default"));

        pokemon.setCries(List.of(cries));
        pokemon.setSprites(List.of(sprites));
        pokemon.setStats(listaStats);
        pokemon.setTypes(listTypes);
        pokemon.setSpecies(speciesML);

        return pokemon;

    }
}
