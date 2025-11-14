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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import risosu.it.PokeApiClient.DTO.PokeDetailDTO;
import risosu.it.PokeApiClient.DTO.PokemonDTO;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.Result;

@Controller
@RequestMapping("/pokeControl")
public class PokeController {
    
    private final String url = "http://localhost:8081//";

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
            List<Pokemon> filtrados = pokemones.stream()
                    .filter(p -> p.getTypes().stream().anyMatch(t -> types.contains(t.getName())))
                    .collect(Collectors.toList());
            resultados = filtrados;
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getLocalizedMessage());
        }
        return resultados;
    }
}
