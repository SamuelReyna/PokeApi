///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package risosu.it.PokeApiClient.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import risosu.it.PokeApiClient.DTO.PokeDetailDTO;
import risosu.it.PokeApiClient.DTO.PokemonDTO;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.Result;


@Controller
@RequestMapping("/pokeControl")
public class PokeController {
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
        public String mostrarVistaFinal(Model model) {
        // Leer el archivo JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Pokemon> pokemones = Arrays.asList(
                mapper.readValue(new File("pokemons.json"), Pokemon[].class)
            );
            model.addAttribute("pokemones", pokemones);
            
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo leer el archivo");
        }

        return "index"; // Thymeleaf buscará vistaFinal.html
    }
}

