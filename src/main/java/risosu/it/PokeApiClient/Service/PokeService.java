///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package risosu.it.PokeApiClient.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import risosu.it.PokeApiClient.DTO.PokemonDTO;
//import risosu.it.PokeApiClient.ML.Result;
//
//@Service    
//public class PokeService {
//    
//    public Result PokeLista(){
//    Result result = new Result();
//        try {
//             RestTemplate restTemplate = new RestTemplate();
//    ResponseEntity<Map<String,Object>> pokeResponse = restTemplate.exchange("https://pokeapi.co/api/v2/pokemon?limit=10&offset=0",
//            HttpMethod.GET, 
//            HttpEntity.EMPTY, 
//            new ParameterizedTypeReference<Map<String,Object>>(){});
//    
//        if (pokeResponse.getStatusCode() == HttpStatusCode.valueOf(200)) {
//           result.object = pokeResponse.getBody().get("results");
//           result.correct=true;
//        }else{
//        result.correct = false;}
//        } catch (Exception e) {
//            result.correct=false;
//            result.errorMessage = e.getLocalizedMessage();
//            result.ex=e;
//            
//        } 
//        return result;
//    }
//    
//       public Result PokeDetail(String urlDetail){
//    Result result = new Result();
//    
//        try {
//             RestTemplate restTemplate = new RestTemplate();
//    ResponseEntity<Map<String,Object>> pokeResponse = restTemplate.exchange(urlDetail,
//            HttpMethod.GET, 
//            HttpEntity.EMPTY, 
//            new ParameterizedTypeReference<Map<String,Object>>(){});
//    
//        if (pokeResponse.getStatusCode() == HttpStatusCode.valueOf(200)) {
//            
//            //Guardamos la respuesta en una variable:
//            Map<String,Object> respuesta = pokeResponse.getBody();
//            
//            //Creamos un nuevo map para almacenar los datos del detail del pokemon:
//            Map<String,Object> pokeDetail = new HashMap<>();
//            
//            pokeDetail.put("id", respuesta.get("id"));
//            pokeDetail.put("height", respuesta.get("height"));
//            pokeDetail.put("weight", respuesta.get("weight"));
//            pokeDetail.put("base_experience", respuesta.get("base_experience"));
//            pokeDetail.put("is_default", respuesta.get("is_default"));
//            
//            //Como los sprites son una lista, no podemos acceder directamente a sus valores por lo que usamos Map:
//             Map<String, Object> sprites = (Map<String, Object>) respuesta.get("sprites");
//            pokeDetail.put("sprite", sprites.get("front_default"));
//            
//             Map<String, Object> cries = (Map<String, Object>) respuesta.get("cries");
//            pokeDetail.put("cries", cries.get("latest"));
//            
//            
//            //Por otro lado, los moves, son una laista de elementos. Cada elemento ces una pequeña lista a su ves:
//            List<Map<String, Object>> moves = (List<Map<String, Object>>) respuesta.get("moves");
//            List<String> moveNames = new ArrayList<>();
//            //Math.min compara ambos valores, y debuelve el mas pequeño, en este caso solo los 3 priemros movimientos del total 
//            //Y si el numero de movimientos es menor, retorna el total que seria el  menor. 
//            for (int i = 0; i < Math.min(3, moves.size()); i++) {
//            Map<String, Object> moveWrapper = moves.get(i);
//            Map<String, Object> move = (Map<String, Object>) moveWrapper.get("move");
//            moveNames.add((String) move.get("name"));
//            }
//            
//            //Tres maps anidados en pocas palabras:
//            pokeDetail.put("moves", moveNames);
//
//            
//            
//            
//           result.object = pokeDetail;
//           result.correct=true;
//        }else{
//        result.correct = false;}
//        } catch (Exception e) {
//            result.correct=false;
//            result.errorMessage = e.getLocalizedMessage();
//            result.ex=e;
//            
//        } 
//        return result;
//    }
//}
