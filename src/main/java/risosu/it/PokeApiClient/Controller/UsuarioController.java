package risosu.it.PokeApiClient.Controller;

import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import risosu.it.PokeApiClient.ML.Entrenador;
import risosu.it.PokeApiClient.DTO.Password;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private final String url = "http://localhost:8081/auth/";

    @GetMapping("/register")
    public String Register() {
        return "register";
    }

    @GetMapping("/login")
    public String Login(@ModelAttribute("Entrenador") Entrenador entrenador) {
        return "login";
    }

    @PostMapping("/login")
    public String Login(@ModelAttribute("Entrenador") Entrenador entrenador, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entrenador> requestEntity = new HttpEntity<>(entrenador, headers);

        ResponseEntity<Map<String, Object>> responseEntity
                = restTemplate.exchange(url + "login",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Map<String, Object>>() {
                }
                );
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            session.setAttribute("token", responseEntity.getBody().get("token"));

            HttpHeaders headers1 = new HttpHeaders();

            HttpEntity<String> entity = new HttpEntity<>(headers1);
            headers1.setContentType(MediaType.APPLICATION_JSON);

            headers1.set("Authorization", "Bearer " + session.getAttribute("token"));

            ResponseEntity<Map<String, Object>> tokenDecode
                    = restTemplate.exchange(url + "decode",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            session.setAttribute("role", (String) tokenDecode.getBody().get("role"));
            session.setAttribute("username", (String) tokenDecode.getBody().get("sub"));
        }

        return "redirect:/pokemon";
    }

    @GetMapping("/verify")
    public String Verify() {
        return "sendEmail";
    }

    @GetMapping("/changePassword")
    public String ChangePassword(@RequestParam(name = "token", required = true) String token, Model model) {
        model.addAttribute("token", token);
        return "ResetPassword";
    }

    @PostMapping("/resetPassword")
    public String ChangePass(RedirectAttributes redirectAttributes, @ModelAttribute("password") Password password, @RequestParam("token") String token) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Password> requestEntity = new HttpEntity<>(password, headers);

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(
                        url + "changePass?token=" + token,
                        HttpMethod.PATCH,
                        requestEntity,
                        String.class);

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            redirectAttributes.addFlashAttribute("message", "Contraseña cambiada exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Algo salió mal");
        }
        return "redirect:/usuario/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("token");
        session.removeAttribute("username");
        session.removeAttribute("role");
        return "redirect:/pokemon";
    }

    @PostMapping("/enviarEmailRecuperacion")
    public String sendEmail(@RequestParam("correo") String correo) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response
                = restTemplate.exchange(url + "sendEmail?email=" + correo,
                        HttpMethod.POST,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<String>() {

                }
                );
        if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return "redirect:/usuario/login";
        }

        return "redirect:/usuario/verify";
    }

}
