package risosu.it.PokeApiClient.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @GetMapping("/register")
    public String Register() {
        return "register";
    }

    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/verify")
    public String Verify() {
        return "sendEmail";
    }

    @GetMapping("/changePassword")
    public String ChangePassword() {
        return "ResetPassword";
    }
}
