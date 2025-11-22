package risosu.it.PokeApiClient.Controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import risosu.it.PokeApiClient.DTO.Password;
import risosu.it.PokeApiClient.Service.PokeService;
import risosu.it.PokeApiClient.ML.Entrenador;
import risosu.it.PokeApiClient.ML.Pokemon;
import risosu.it.PokeApiClient.ML.Type;

@Controller
@RequestMapping("/pokeControl/admin")
public class EntrenadorController {

    @Autowired
    private PokeService pokeservice;

    private final String url = "http://localhost:8081/api/entrenador";

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model, @ModelAttribute("entrenador") Entrenador entrenador) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/pokemon";

        } else if (session.getAttribute("token") != null) {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Integer> responseEntity
                    = restTemplate.exchange(url + "/count", HttpMethod.GET, HttpEntity.EMPTY, Integer.class);
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("totalUsers", responseEntity.getBody());
            }
            ResponseEntity<Integer> countPokemons
                    = restTemplate.exchange("http://localhost:8081/api/pokemon/totals", HttpMethod.GET, HttpEntity.EMPTY, Integer.class);
            if (countPokemons.getStatusCode() == HttpStatusCode.valueOf(200)) {
                model.addAttribute("totalPokemons", countPokemons.getBody());
            }

            List<Pokemon> pokefavs = pokeservice.getFavTypes();

            Map<String, Long> typeCount
                    = pokefavs.stream()
                            .flatMap(p -> p.getTypes().stream())
                            .collect(Collectors.groupingBy(Type::getName, Collectors.counting()));
            model.addAttribute("size", typeCount.size());

            model.addAttribute("types", typeCount);
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("role", session.getAttribute("role"));
        }
        return "adminDashboard";
    }

    @GetMapping("/usuarios")
    public String usuario(Model model, HttpSession session, @ModelAttribute("entrenador") Entrenador entrenador) {
        RestTemplate restTemplate = new RestTemplate();
        String role = (String) session.getAttribute("role");

        if (!role.equals("[ROLE_Admin]")) {
            return "redirect:/pokeControl/admin/dashboard";
        }

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

    @PostMapping("/usuarios/password")
    public String password(RedirectAttributes redirectAttributes, @RequestParam("idEntrenador") int idEntrenador, @RequestParam("username") String username, Model model, @ModelAttribute("password") Password password) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        if (!password.getPassword().equals(password.getConfirmPassword())) {
            redirectAttributes.addAttribute("message", "Las contraseñas no coinciden");
            return "redirect:/usuario/ajustes/" + username;
            // manejar error
        }
        HttpEntity<Password> entity = new HttpEntity<>(password);
        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange("http://localhost:8081/api/entrenador/" + idEntrenador + "/password",
                        HttpMethod.PATCH,
                        entity, new ParameterizedTypeReference<Entrenador>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return "redirect:/usuario/ajustes/" + username;

        }

        return "redirect:/password/" + username;
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
