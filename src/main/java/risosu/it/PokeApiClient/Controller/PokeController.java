///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package risosu.it.PokeApiClient.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import risosu.it.PokeApiClient.DTO.PokeDetailDTO;
import risosu.it.PokeApiClient.DTO.PokemonDTO;
import risosu.it.PokeApiClient.ML.Result;


@Controller
@RequestMapping("/pokeInicio")
public class PokeController {
    
//    @Autowired
//    private PokeService pokeService;
//    
//   @GetMapping()
//public String Inicio(Model model) {
//
//    Result result = pokeService.PokeLista();
//
//    if (!result.correct) {
//        model.addAttribute("error", "No se pudo obtener la lista de Pokémon");
//        return "error";
//    }
//
//    ObjectMapper mapper = new ObjectMapper();
//    List<LinkedHashMap<String, Object>> rawList = (List<LinkedHashMap<String, Object>>) result.object;
//    List<PokemonDTO> pokemones = new ArrayList<>();
//
//    for (LinkedHashMap<String, Object> raw : rawList) {
//        PokemonDTO dto = mapper.convertValue(raw, PokemonDTO.class);
//        pokemones.add(dto);
//    }
//
//    
//    // Ahora agregamos el detail a cada uno
//    for (PokemonDTO pokemon : pokemones) {
//        Result resultDetail = pokeService.PokeDetail(pokemon.getUrl());
//        if (resultDetail.correct && resultDetail.object != null) {
//            PokeDetailDTO detail = mapper.convertValue(resultDetail.object, PokeDetailDTO.class);
//            pokemon.setDetail(detail);
//        }
//    }
//
//    model.addAttribute("pokemons", pokemones);
//    
//    return "index";
//}
}

