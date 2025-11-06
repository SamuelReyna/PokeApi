package risosu.it.PokeApiClient.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.Service.EntrenadorService;
import risosu.it.PokeApiClient.Component.JwtUtil;

@RestController
@RequestMapping("auth")
public class AuthController {

    public AuthController(risosu.it.PokeApiClient.Service.EntrenadorService entrenadorService, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.entrenadorService = entrenadorService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    private final EntrenadorService entrenadorService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody Entrenador entrenador) {
        UserDetails user = entrenadorService.loadEntrenadorByUsername(entrenador.getUsername());

        if (user == null || !passwordEncoder.matches(entrenador.getPassword(), user.getPassword())) {
            HashMap<String, Object> message = new HashMap();
            message.put("errorMessage", "Usuario o contraseña incorrectos");

            return ResponseEntity.badRequest().body(message);
        }
        HashMap<String, Object> response = new HashMap<>();

        String jwt = null;
        jwt = jwtUtil.generateToken(user.getUsername(), user.getAuthorities().toString());
        response.put("token", jwt);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/decode")
    public Map<String, Object> Decode(@RequestHeader("Authorization") String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);
            Jws<Claims> claims = jwtUtil.validateToken(jwt);
            return claims.getBody();
        } else {
            throw new IllegalArgumentException("Token inválido o ausente");
        }
    }
}
