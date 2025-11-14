package risosu.it.PokeApiClient.Controller;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import risosu.it.PokeApiClient.ML.Entrenador;
import risosu.it.PokeApiClient.ML.Rol;

@Controller
@RequestMapping("/pokeControl/admin")
public class EntrenadorController {

    private final String url = "http://localhost:8081/api/entrenador";

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/pokemon";

        } else if (session.getAttribute("token") != null) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("role", session.getAttribute("role"));
        }
        return "adminDashboard";
    }

    @GetMapping("/usuarios")
    public String usuario(Model model, HttpSession session, @ModelAttribute("entrenador") Entrenador entrenador) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Entrenador>> responseEntity
                = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<Entrenador>>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            List<Entrenador> entrenadores = (List<Entrenador>) responseEntity.getBody();
            model.addAttribute("Entrenadores", entrenadores);
        }
        String token = (String) session.getAttribute("token");

        if (token == null) {
            return "redirect:/pokemon";

        } else if (session.getAttribute("token") != null) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("role", session.getAttribute("role"));
        }

        return "UsuariosDashboard";
    }

    @PostMapping("/usuarios/add")
    public String UsuarioAdd(RedirectAttributes redirectAttributes, @ModelAttribute("entrenador") Entrenador entrenador) {
        entrenador.setPassword("1234");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entrenador> requestEntity = new HttpEntity<>(entrenador, headers);
        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange(url,
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Entrenador>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            redirectAttributes.addFlashAttribute("message", "Éxito al guardar usuario");
        }
        return "redirect:/pokeControl/admin/usuarios";
    }

    @PostMapping("/usuarios/update")
    public String Update(RedirectAttributes redirectAttributes, @ModelAttribute("entrenador") Entrenador entrenador) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entrenador> requestEntity = new HttpEntity<>(entrenador, headers);
        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange(url + "/" + entrenador.getIdEntrenador(),
                        HttpMethod.PATCH,
                        requestEntity,
                        new ParameterizedTypeReference<Entrenador>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            redirectAttributes.addFlashAttribute("message", "Éxito al guardar usuario");
        }
        return "redirect:/pokeControl/admin/usuarios";
    }

    @GetMapping("/usuarios/delete/{idEntrenador}")
    public String Delete(@PathVariable("idEntrenador") int idEntrenador, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entrenador> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange(url + "/" + idEntrenador,
                        HttpMethod.DELETE,
                        requestEntity,
                        new ParameterizedTypeReference<Entrenador>() {
                }
                );
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            redirectAttributes.addFlashAttribute("message", "Éxito al guardar usuario");
        }

        return "redirect:/pokeControl/admin/usuarios";
    }

}
