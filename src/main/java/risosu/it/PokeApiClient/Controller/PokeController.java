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
//    private final String url = "http://localhost:8081//";
//Este controlador es accedido desde vista "loading" para validar si existe ya el archivo JSON y redirigir:

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
    public String mostrarVistaFinal(Model model, HttpSession session) {
        // Leer el archivo JSON
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Pokemon> pokemones = Arrays.asList(
                    mapper.readValue(new File("pokemons.json"), Pokemon[].class)
            );

            model.addAttribute("pokemones", pokemones);
            model.addAttribute("token", session.getAttribute("token"));

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

        ObjectMapper mapper = new ObjectMapper();
        List<Pokemon> resultados = new ArrayList<>();

        try {
            List<Pokemon> pokemones = Arrays.asList(
                    mapper.readValue(new File("pokemons.json"), Pokemon[].class));

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

        ObjectMapper mapper = new ObjectMapper();
        List<Pokemon> resultados = new ArrayList<>();

        try {

            List<Pokemon> pokemones = Arrays.asList(
                    mapper.readValue(new File("pokemons.json"), Pokemon[].class));
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
        } catch (IOException e) {
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
            if (root == null) {
                throw new RuntimeException("JSON raíz viene null");
            }

            Map<String, Object> obj = root.get(0);
            // Obtener pokedex
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
//            poke.stream().filter
//        (p -> pokemons.forEach
//        (pokemons.get(0)
//                .get("idJson"))
//                .contains(p.getId())).toList();
            model.addAttribute("pokedex", pokedex);
            model.addAttribute("pokemones", poke);
            model.addAttribute("entrenador", entrenador);
        }
        return "usuario";
    }

}
