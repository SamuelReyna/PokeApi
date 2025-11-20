package risosu.it.PokeApiClient.Controller;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import risosu.it.PokeApiClient.DTO.LoginDTO;

import risosu.it.PokeApiClient.ML.Entrenador;
import risosu.it.PokeApiClient.DTO.Password;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private final String url = "http://localhost:8081/auth/";

    @GetMapping("/register")
    public String Register(@ModelAttribute("entrenador") Entrenador entrenador) {
        return "register";
    }

    @PostMapping("/register")
    public String RegisterPost(@ModelAttribute("entrenador") Entrenador entrenador, RedirectAttributes redirectAttributes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entrenador> requestEntity = new HttpEntity<>(entrenador, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map<String, Object>> responseEntity
                = restTemplate.exchange(url + "register",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Map<String, Object>>() {
                }
                );
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return "redirect:/usuario/checkEmail";

        } else {
            redirectAttributes.addFlashAttribute("message", "Error al crear cuenta");
            return "redirect:/usuario/login";
        }

    }

    @GetMapping("/checkEmail")
    public String CheckEmail() {
        return "SendingFile";
    }

    @GetMapping("/login")
    public String Login(@ModelAttribute("Entrenador") Entrenador entrenador) {
        return "login";
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange("http://localhost:8081/api/entrenador/" + username + "/username", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Entrenador>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            Entrenador entrenador = responseEntity.getBody();
            model.addAttribute("entrenador", entrenador);
        }

        return "profile";
    }

    @GetMapping("/ajustes/{username}")
    public String ajustes(@PathVariable("username") String username, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Entrenador> responseEntity
                = restTemplate.exchange("http://localhost:8081/api/entrenador/" + username + "/username", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Entrenador>() {
                });
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            Entrenador entrenador = responseEntity.getBody();
            model.addAttribute("entrenador", entrenador);
        }

        return "password";
    }

    @PostMapping("/login")
    public String Login(@ModelAttribute("Entrenador") Entrenador entrenador,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        LoginDTO loginDTO = new LoginDTO();

        loginDTO.setUsername(entrenador.getUsername());
        loginDTO.setPassword(entrenador.getPassword());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    url + "login",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
            }
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                session.setAttribute("token", responseEntity.getBody().get("token"));

                // Decodificar token
                HttpHeaders headers1 = new HttpHeaders();
                headers1.setContentType(MediaType.APPLICATION_JSON);
                headers1.set("Authorization", "Bearer " + session.getAttribute("token"));

                HttpEntity<String> entity = new HttpEntity<>(headers1);

                ResponseEntity<Map<String, Object>> tokenDecode = restTemplate.exchange(
                        url + "decode",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Map<String, Object>>() {
                }
                );

                session.setAttribute("role", tokenDecode.getBody().get("role"));
                session.setAttribute("username", tokenDecode.getBody().get("sub"));

                return "redirect:/pokemon";
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Aquí capturas 400, 401, 403, 500, etc.
            redirectAttributes.addFlashAttribute("message", "Usuario o contraseña incorrectos");
            return "redirect:/usuario/login";
        } catch (Exception e) {
            // Cualquier otro error
            redirectAttributes.addFlashAttribute("message", "Error interno al iniciar sesión");
            return "redirect:/usuario/login";
        }

        // Fallback (no debería llegar aquí)
        redirectAttributes.addFlashAttribute("message", "Error desconocido");
        return "redirect:/usuario/login";
    }

    @GetMapping("/verify")
    public String Verify() {
        return "sendEmail";
    }

    @GetMapping("/verifyAccount")
    public String VerifyAccount(@RequestParam(name = "token", required = true) String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url + "verifyAccount?token=" + token,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        String.class
                );
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) {
            return "verifyAccount";
        } else {
            return "errorVerificar";
        }
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
