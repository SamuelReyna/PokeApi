///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package risosu.it.PokeApiClient.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.Entrenador;
import risosu.it.PokeApiClient.ML.Type;
import risosu.it.PokeApiClient.Service.PokeService;

@Controller
@RequestMapping("/pokeControl")
public class PokeController {

    @Autowired
    private PokeService pokeService;
//Este controlador es accedido desde vista "loading" para validar si existe ya el archivo JSON y redirigir:
    private Map<String, Boolean> sortOrder = new HashMap<>();

    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> verificarEstado() {
        File archivo = new File("pokemons.json");
        Map<String, Object> estado = new HashMap<>();
        estado.put("ready", archivo.exists());
        return estado;
    }

    //Este controlador se encarga de mostrar la lista de pokemons:
    @GetMapping("/listar")
    public String mostrarVistaFinal(Model model,
            HttpSession session,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "cant", defaultValue = "20", required = false) int cant) {
        // Leer el archivo JSON
        ObjectMapper mapper = new ObjectMapper();

        try {

            List<Pokemon> pokemones = Arrays.asList(
                    mapper.readValue(new File("pokemons.json"), Pokemon[].class)
            );
            int totalPokemones = pokemones.size();
            int totalPages;
            if (cant == 0) {
                totalPages = 1;
            } else {
                totalPages = (int) Math.ceil((double) totalPokemones / cant);

            }

            if (page > 0 || cant > 0) {
                pokemones = pokemones.stream().sorted((p1, p2) -> {
                    int comparison;
                    if (p1.getOrder() <= 0 && p2.getOrder() <= 0) {
                        comparison = Integer.compare(p1.getOrder(), p2.getOrder());
                    } else if (p1.getOrder() <= 0) {
                        comparison = 1;
                    } else if (p2.getOrder() <= 0) {
                        comparison = -1;
                    } else {
                        comparison = Integer.compare(p1.getOrder(), p2.getOrder());
                    }
                    return comparison;
                }).collect(Collectors.toList());
                // Calcular totales
                totalPokemones = pokemones.size();
                totalPages = (int) Math.ceil((double) totalPokemones / cant);
                if (page < 0) {
                    page = 0;
                }
                if (page >= totalPages) {
                    page = totalPages - 1;
                }
                int from = page * cant;
                int to = Math.min(from + cant, totalPokemones);
                pokemones = pokemones.subList(from, to);
            }
            pokeService.setGetByPageAndQuantity(pokemones);

            model.addAttribute("currentPage", page);          // Página actual (0-indexed)
            model.addAttribute("pageSize", cant);             // Cantidad por página
            model.addAttribute("totalPages", totalPages);     // Total de páginas   
            model.addAttribute("pokemones", pokemones);
            model.addAttribute("token", session.getAttribute("token"));
            model.addAttribute("totalElements", totalPokemones); // Total de elementos
            if (session.getAttribute("token") != null) {
                model.addAttribute("username", session.getAttribute("username"));
                model.addAttribute("role", session.getAttribute("role"));
            }

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo leer el archivo");
        }

        return "index";
    }

    @GetMapping("/searchByName")
    @ResponseBody
    public List<Pokemon> SearchByName(@RequestParam String pokeBusqueda) {

        List<Pokemon> resultados = new ArrayList<>();

        try {
            List<Pokemon> pokemones = pokeService.getGetByPageAndQuantity();

            resultados = pokemones.stream()
                    .filter(pokemon -> pokemon.getName()
                    .toLowerCase(Locale.ITALY)
                    .contains(pokeBusqueda.toLowerCase()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;

    }

    @GetMapping("/searchByTypes")
    @ResponseBody
    public List<Pokemon> FilterByTypes(@RequestParam(required = false) List<String> types) {

        List<Pokemon> resultados = new ArrayList<>();

        try {

            List<Pokemon> pokemones = pokeService.getGetByPageAndQuantity();

            if (types == null) {
                return pokemones;
            }
//            List<Pokemon> filtrados = pokemones.stream()
//                    .filter(p -> p.getTypes().stream().anyMatch(t -> types.contains(t.getName())))
//                    .collect(Collectors.toList());
            resultados = pokemones.stream()
                    .filter(p -> {
                        List<String> pokeTypes = p.getTypes().stream()
                                .map(Type::getName).sorted().collect(Collectors.toList());
                        List<String> searchTypes = types.stream()
                                .sorted()
                                .collect(Collectors.toList());
                        return pokeTypes.equals(searchTypes);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;
    }

    @GetMapping("/orderBy")
    @ResponseBody
    public List<Pokemon> OrderBy(@RequestParam(required = false) String campo) {

        List<Pokemon> resultados = new ArrayList<>();

        try {

            List<Pokemon> pokemones = pokeService.getGetByPageAndQuantity();

            if (campo == null) {
                return pokemones;
            }

// En tu método:
            resultados = pokemones.stream()
                    .sorted((p1, p2) -> {
                        int comparison;
                        String campoLower = campo.toLowerCase();

                        // Obtener el estado actual del toggle para este campo (default true = ASC)
                        boolean isAsc = sortOrder.getOrDefault(campoLower, true);

                        switch (campoLower) {
                            case "name":
                                comparison = p1.getName().compareToIgnoreCase(p2.getName());
                                return isAsc ? comparison : -comparison;

                            case "type":
                                String tipo1 = p1.getTypes().get(0).getName();
                                String tipo2 = p2.getTypes().get(0).getName();
                                comparison = tipo1.compareToIgnoreCase(tipo2);
                                return isAsc ? comparison : -comparison;

                            case "medidas":
                                comparison = Integer.compare(p1.getHeight(), p2.getHeight());
                                return isAsc ? comparison : -comparison;

                            case "order":
                                if (p1.getOrder() <= 0 && p2.getOrder() <= 0) {
                                    comparison = Integer.compare(p1.getOrder(), p2.getOrder());
                                } else if (p1.getOrder() <= 0) {
                                    comparison = 1;
                                } else if (p2.getOrder() <= 0) {
                                    comparison = -1;
                                } else {
                                    comparison = Integer.compare(p1.getOrder(), p2.getOrder());
                                }
                                return isAsc ? comparison : -comparison;

                            default:
                                return 0;
                        }
                    })
                    .collect(Collectors.toList());

// Toggle independiente para este campo específico
            sortOrder.put(campo.toLowerCase(), !sortOrder.getOrDefault(campo.toLowerCase(), true));
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;
    }

    @GetMapping("/searchByNameFavs")
    @ResponseBody
    public List<Pokemon> SearchByNameFavs(@RequestParam String pokeBusqueda) {

        List<Pokemon> resultados = new ArrayList<>();

        try {
            List<Pokemon> pokemones = pokeService.getFavsByEntrenador();

            resultados = pokemones.stream()
                    .filter(pokemon -> pokemon.getName()
                    .toLowerCase(Locale.ITALY)
                    .contains(pokeBusqueda.toLowerCase()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;

    }

    @GetMapping("/searchByTypesFavs")
    @ResponseBody
    public List<Pokemon> FilterByTypesFavs(@RequestParam(required = false) List<String> types) {

        List<Pokemon> resultados = new ArrayList<>();

        try {

            List<Pokemon> pokemones = pokeService.getFavsByEntrenador();

            if (types == null) {
                return pokemones;
            }
//            List<Pokemon> filtrados = pokemones.stream()
//                    .filter(p -> p.getTypes().stream().anyMatch(t -> types.contains(t.getName())))
//                    .collect(Collectors.toList());
            resultados = pokemones.stream()
                    .filter(p -> {
                        List<String> pokeTypes = p.getTypes().stream()
                                .map(Type::getName).sorted().collect(Collectors.toList());
                        List<String> searchTypes = types.stream()
                                .sorted()
                                .collect(Collectors.toList());
                        return pokeTypes.equals(searchTypes);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;
    }

    @GetMapping("/orderByFavs")
    @ResponseBody
    public List<Pokemon> OrderByFavs(@RequestParam(required = false) String campo) {

        List<Pokemon> resultados = new ArrayList<>();

        try {

            List<Pokemon> pokemones = pokeService.getFavsByEntrenador();

            if (campo == null) {
                return pokemones;
            }

// En tu método:
            resultados = pokemones.stream()
                    .sorted((p1, p2) -> {
                        int comparison;
                        String campoLower = campo.toLowerCase();

                        // Obtener el estado actual del toggle para este campo (default true = ASC)
                        boolean isAsc = sortOrder.getOrDefault(campoLower, true);

                        switch (campoLower) {
                            case "name":
                                comparison = p1.getName().compareToIgnoreCase(p2.getName());
                                return isAsc ? comparison : -comparison;

                            case "type":
                                String tipo1 = p1.getTypes().get(0).getName();
                                String tipo2 = p2.getTypes().get(0).getName();
                                comparison = tipo1.compareToIgnoreCase(tipo2);
                                return isAsc ? comparison : -comparison;

                            case "medidas":
                                comparison = Integer.compare(p1.getHeight(), p2.getHeight());
                                return isAsc ? comparison : -comparison;

                            case "order":
                                if (p1.getOrder() <= 0 && p2.getOrder() <= 0) {
                                    comparison = Integer.compare(p1.getOrder(), p2.getOrder());
                                } else if (p1.getOrder() <= 0) {
                                    comparison = 1;
                                } else if (p2.getOrder() <= 0) {
                                    comparison = -1;
                                } else {
                                    comparison = Integer.compare(p1.getOrder(), p2.getOrder());
                                }
                                return isAsc ? comparison : -comparison;

                            default:
                                return 0;
                        }
                    })
                    .collect(Collectors.toList());

// Toggle independiente para este campo específico
            sortOrder.put(campo.toLowerCase(), !sortOrder.getOrDefault(campo.toLowerCase(), true));
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, Model model, HttpSession session) {

        model.addAttribute("token", session.getAttribute("token"));
        if (session.getAttribute("token") != null) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("role", session.getAttribute("role"));
        } else {
            return "redirect:/pokemon";
        }
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange("http://localhost:8081/api/entrenador/" + username + "/username", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Entrenador>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {

            Entrenador entrenador = responseEntity.getBody();
            Long id = entrenador.getIdEntrenador();

            // Leer la respuesta como String (debug)
//            ResponseEntity<String> raw = restTemplate.getForEntity(
//                    "http://localhost:8081/api/entrenador/favorites/" + id,
//                    String.class
//            );
//            System.out.println("JSON recibido:\n" + raw.getBody());
            // Ahora sí leer como Map genérico (raíz es objeto, NO lista)
            ResponseEntity<List<Map<String, Object>>> response
                    = restTemplate.exchange(
                            "http://localhost:8081/api/entrenador/favorites/" + id,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    }
                    );

            List<Map<String, Object>> root = (List<Map<String, Object>>) response.getBody();
            if (root.isEmpty()) {
                model.addAttribute("pokedex", null);
                model.addAttribute("entrenador", entrenador);
                model.addAttribute("pokemones", new ArrayList<>());
                model.addAttribute("countType", 0);
                model.addAttribute("types", null);
                model.addAttribute("count", 0);
            } else {
                Map<String, Object> obj = root.get(0);
                Map<String, Object> pokedex = (Map<String, Object>) obj.get("pokedex");
                if (pokedex == null) {
                    throw new RuntimeException("El campo 'pokedex' viene null.");
                }

                // Obtener pokedexPokemons
                List<Map<String, Object>> pokedexPokemons
                        = (List<Map<String, Object>>) pokedex.get("pokedexPokemons");

                if (pokedexPokemons == null) {
                    throw new RuntimeException("El campo 'pokedexPokemons' viene null.");
                }

                // Sacar solo los pokemones
                List<Map<String, Object>> pokemons
                        = pokedexPokemons.stream()
                                .map(item -> (Map<String, Object>) item.get("pokemon"))
                                .filter(Objects::nonNull)
                                .toList();
                List<Pokemon> poke = pokeService.getFavTypes();
                List<Integer> idsJson = pokemons.stream()
                        .map(p -> (Integer) p.get("idJson"))
                        .toList();
                poke = poke.stream().filter(p -> idsJson.contains(p.getId())).toList();
                pokeService.setFavsByEntrenador(poke);
                Map<String, Long> typeCount = poke.stream()
                        .flatMap((p -> p.getTypes().stream()))
                        .collect(Collectors.groupingBy(Type::getName, Collectors.counting()));
                model.addAttribute("countType", typeCount.size());
                model.addAttribute("types", typeCount);
                model.addAttribute("count", poke.size());
                model.addAttribute("pokedex", pokedex);
                model.addAttribute("pokemones", poke);
                model.addAttribute("entrenador", entrenador);
            }
        }
        return "usuario";

    }

}
