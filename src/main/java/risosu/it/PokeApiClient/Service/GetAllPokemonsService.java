package risosu.it.PokeApiClient.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.PokemonListResponse;
import risosu.it.PokeApiClient.ML.Result;

@Service
public class GetAllPokemonsService {

    private final WebClient webClient;
    private final Semaphore rateLimiter = new Semaphore(10);
    private final ExecutorService executor = Executors.newFixedThreadPool(20);

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
            
            List<Pokemon> buffer = new ArrayList<>();
            for (CompletableFuture<Pokemon> f : pokemones) {
                Pokemon p = f.get();
                if (p != null) {
                    buffer.add(p);
                }
                if (buffer.size() >= 50) {
                    buffer.clear();
                }
            }
            if (!buffer.isEmpty()) {
            }
            System.out.println("✅ Persistencia completada");
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

        pokemon.setId((Long) (Number) resp.get("id"));
        pokemon.setName((String) resp.get("name"));
        pokemon.setHeight((String) resp.get("height"));
        pokemon.setWeight((String) resp.get("weight"));

        return pokemon;

    }
}
