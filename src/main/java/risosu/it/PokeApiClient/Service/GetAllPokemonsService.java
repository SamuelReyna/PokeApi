package risosu.it.PokeApiClient.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import risosu.it.PokeApiClient.ML.Cries;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.PokemonListResponse;
import risosu.it.PokeApiClient.ML.Result;
import risosu.it.PokeApiClient.ML.Sprite;
import risosu.it.PokeApiClient.ML.Stats;
import risosu.it.PokeApiClient.ML.Type;

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

    public void persistirPokemones() throws Exception {
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
                }
            }, executor).whenComplete((p, ex) -> {
                int done = counter.incrementAndGet();
                if (done % 50 == 0) {
                    System.out.println("Procesados " + done + "/" + listaPokemones.size());
                }
            });

            pokemones.add(fut);

//            List<Pokemon> buffer = new ArrayList<>();
//            for (CompletableFuture<Pokemon> f : pokemones) {
//                Pokemon p = f.get();
//                if (p != null) {
//                    buffer.add(p);
//                }
//                if (buffer.size() >= 50) {
//                    buffer.clear();
//                }
//            }
            List<Pokemon> pokemons = pokemones.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("pokemons.json"), pokemons);
            System.out.println("✅ Archivo 'pokemons.json' creado con " + pokemons.size() + " pokemones");
        }

    }

    private Pokemon
            obtenerDetallesPokemon(String url) {
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

        return pokemon;

    }
}
