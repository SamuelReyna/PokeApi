package risosu.it.PokeApiClient.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.Service.EntrenadorService;
import risosu.it.PokeApiClient.Component.JwtUtil;
import risosu.it.PokeApiClient.Service.PasswordResetTokenService;
import risosu.it.PokeApiClient.Service.VerifyTokenService;
import risosu.it.PokeApiClient.Service.EmailService;
import risosu.it.PokeApiClient.DTO.Password;

@RestController
@RequestMapping("auth")
public class AuthController {

    public AuthController(EmailService emailService, risosu.it.PokeApiClient.Service.EntrenadorService entrenadorService, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder, JwtUtil jwtUtil, risosu.it.PokeApiClient.Service.PasswordResetTokenService passwordResetTokenService, risosu.it.PokeApiClient.Service.VerifyTokenService verifyTokenService) {
        this.entrenadorService = entrenadorService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.passwordResetTokenService = passwordResetTokenService;
        this.verifyTokenService = verifyTokenService;
        this.emailService = emailService;
    }

    private final EntrenadorService entrenadorService;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetTokenService passwordResetTokenService;

    private final VerifyTokenService verifyTokenService;

    private final EmailService emailService;

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

    @PostMapping("/sendEmail")
    public ResponseEntity sendEmail(@RequestParam("email") String email) throws Exception {

        Entrenador entrenador = entrenadorService.loadByCorreo(email);
        if (entrenador != null) {

            String token = passwordResetTokenService.GenerateToken(entrenador.getIdEntrenador());

            String linkRestablecer = "http://localhost:8081/changePassword?token=" + token;

            String html = """
                              <!DOCTYPE html>
                              <html lang="es">
                              <head>
                                  <meta charset="UTF-8">
                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                  <title>Restablecer Contrase\u00f1a</title>
                                  <style>
                                      body {
                                          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                          background-color: #f4f4f4;
                                          margin: 0;
                                          padding: 0;
                                      }
                                      .container {
                                          max-width: 600px;
                                          margin: 40px auto;
                                          background-color: #ffffff;
                                          border-radius: 10px;
                                          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                          overflow: hidden;
                                      }
                                      .header {
                                          background-color: #007bff;
                                          color: #ffffff;
                                          padding: 20px;
                                          text-align: center;
                                      }
                                      .content {
                                          padding: 30px;
                                          color: #333333;
                                          line-height: 1.6;
                                      }
                                      .button {
                                          display: inline-block;
                                          padding: 12px 24px;
                                          margin: 20px 0;
                                          background-color: #007bff;
                                          color: #ffffff;
                                          text-decoration: none;
                                          border-radius: 6px;
                                          font-weight: bold;
                                          transition: background-color 0.3s ease;
                                      }
                                      .button:hover {
                                          background-color: #0056b3;
                                      }
                                      .footer {
                                          text-align: center;
                                          font-size: 12px;
                                          color: #999999;
                                          padding: 20px;
                                          border-top: 1px solid #eeeeee;
                                      }
                                  </style>
                              </head>
                              <body>
                                  <div class="container">
                                      <div class="header">
                                          <h1>\u00a1Hola """ + entrenador.getUsername() + "!</h1>\n"
                    + "        </div>\n"
                    + "        <div class=\"content\">\n"
                    + "            <p>Recibimos una solicitud para restablecer tu contraseña.</p>\n"
                    + "            <p>Haz clic en el botón de abajo para crear una nueva contraseña segura:</p>\n"
                    + "            <a href=\"" + linkRestablecer + "\" class=\"button\">Restablecer Contraseña</a>\n"
                    + "            <p>Si no realizaste esta solicitud, puedes ignorar este mensaje. Tu cuenta seguirá siendo segura.</p>\n"
                    + "        </div>\n"
                    + "        <div class=\"footer\">\n"
                    + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                    + "        </div>\n"
                    + "    </div>\n"
                    + "</body>\n"
                    + "</html>";

            emailService.sendEmail(entrenador.getCorreo(), "Solicitud de recuperación de contraseña", html);

        }

        return ResponseEntity.status(200).body(entrenador);
    }

    @PatchMapping("/changePass")
    public ResponseEntity changePass(@RequestBody Password password, @RequestParam("token") String token) {
        if (token != null) {
            if (passwordResetTokenService.validarToken(token)) {
                int idUser = passwordResetTokenService.getUserIdbyToken(token);

                if (idUser > 0) {
                    Optional<Entrenador> entrenador = entrenadorService.GetById(Long.valueOf(idUser));
                    if (entrenador.isPresent()) {
                        if (password.getPassword() == null ? password.getConfirmPassword() == null : password.getPassword().equals(password.getConfirmPassword())) {
                            entrenador.get().setPassword(passwordEncoder.encode(password.getPassword()));
                            Entrenador resultUpdate = entrenadorService.Update(entrenador.get());

                            String html = """
                                              <!DOCTYPE html>
                                              <html lang="es">
                                              <head>
                                                  <meta charset="UTF-8">
                                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                  <title>Contrase\u00f1a Actualizada</title>
                                                  <style>
                                                      body {
                                                          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                                          background-color: #f4f4f4;
                                                          margin: 0;
                                                          padding: 0;
                                                      }
                                                      .container {
                                                          max-width: 600px;
                                                          margin: 40px auto;
                                                          background-color: #ffffff;
                                                          border-radius: 10px;
                                                          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                                          overflow: hidden;
                                                      }
                                                      .header {
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          padding: 20px;
                                                          text-align: center;
                                                      }
                                                      .content {
                                                          padding: 30px;
                                                          color: #333333;
                                                          line-height: 1.6;
                                                      }
                                                      .button {
                                                          display: inline-block;
                                                          padding: 12px 24px;
                                                          margin: 20px 0;
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          text-decoration: none;
                                                          border-radius: 6px;
                                                          font-weight: bold;
                                                          transition: background-color 0.3s ease;
                                                      }
                                                      .button:hover {
                                                          background-color: #218838;
                                                      }
                                                      .footer {
                                                          text-align: center;
                                                          font-size: 12px;
                                                          color: #999999;
                                                          padding: 20px;
                                                          border-top: 1px solid #eeeeee;
                                                      }
                                                  </style>
                                              </head>
                                              <body>
                                                  <div class="container">
                                                      <div class="header">
                                                          <h1>Contrase\u00f1a Actualizada</h1>
                                                      </div>
                                                      <div class="content">
                                                          <p>\u00a1Hola   """ // Verde éxito
                                    + resultUpdate.getUsername() + "!</p>\n"
                                    + "            <p>Queremos informarte que tu contraseña ha sido cambiada exitosamente.</p>\n"
                                    + "            <p>Si tú realizaste este cambio, no necesitas hacer nada más.</p>\n"
                                    + "            <p>Si <strong>no fuiste tú</strong>, te recomendamos restablecer tu contraseña de inmediato o contactar con el soporte técnico.</p>\n"
                                    + "        </div>\n"
                                    + "        <div class=\"footer\">\n"
                                    + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                                    + "        </div>\n"
                                    + "    </div>\n"
                                    + "</body>\n"
                                    + "</html>";
                            emailService.sendEmail(resultUpdate.getCorreo(), "Actualización de contraseña", html);

                        }
                    }
                }
            } else {
                return (ResponseEntity) ResponseEntity.badRequest();
            }
        } else {
            return (ResponseEntity) ResponseEntity.badRequest();

        }
            return ResponseEntity.status(200).body("exito");
    }

    @PostMapping("/sendVerifyEmail")
    public ResponseEntity sendVerifyEmail(@RequestParam("email") String email) {
        Entrenador entrenador = (Entrenador) entrenadorService.loadByCorreo(email);
        if (entrenador != null) {
            String token = verifyTokenService.GenerateToken(entrenador.getIdEntrenador());

            // 🔗 Enlace de verificación
            String linkVerificar = "http://localhost:8080/auth/verifyAccount?token=" + token;

            String html = """
                          <!DOCTYPE html>
                          <html lang="es">
                          <head>
                              <meta charset="UTF-8">
                              <meta name="viewport" content="width=device-width, initial-scale=1.0">
                              <title>Verificaci\u00f3n de Cuenta</title>
                              <style>
                                  body {
                                      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                      background-color: #f4f4f4;
                                      margin: 0;
                                      padding: 0;
                                  }
                                  .container {
                                      max-width: 600px;
                                      margin: 40px auto;
                                      background-color: #ffffff;
                                      border-radius: 10px;
                                      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                      overflow: hidden;
                                  }
                                  .header {
                                      background-color: #28a745;
                                      color: #ffffff;
                                      padding: 20px;
                                      text-align: center;
                                  }
                                  .content {
                                      padding: 30px;
                                      color: #333333;
                                      line-height: 1.6;
                                  }
                                  .button {
                                      display: inline-block;
                                      padding: 12px 24px;
                                      margin: 20px 0;
                                      background-color: #28a745;
                                      color: #ffffff;
                                      text-decoration: none;
                                      border-radius: 6px;
                                      font-weight: bold;
                                      transition: background-color 0.3s ease;
                                  }
                                  .button:hover {
                                      background-color: #1e7e34;
                                  }
                                  .footer {
                                      text-align: center;
                                      font-size: 12px;
                                      color: #999999;
                                      padding: 20px;
                                      border-top: 1px solid #eeeeee;
                                  }
                              </style>
                          </head>
                          <body>
                              <div class="container">
                                  <div class="header">
                                      <h1>\u00a1Bienvenido """ + entrenador.getUsername() + "!</h1>\n"
                    + "        </div>\n"
                    + "        <div class=\"content\">\n"
                    + "            <p>Gracias por registrarte en nuestra aplicación.</p>\n"
                    + "            <p>Antes de comenzar, necesitamos verificar tu dirección de correo electrónico.</p>\n"
                    + "            <p>Haz clic en el siguiente botón para activar tu cuenta:</p>\n"
                    + "            <a href=\"" + linkVerificar + "\" class=\"button\">Verificar mi cuenta</a>\n"
                    + "            <p>Si no creaste esta cuenta, puedes ignorar este mensaje.</p>\n"
                    + "        </div>\n"
                    + "        <div class=\"footer\">\n"
                    + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                    + "        </div>\n"
                    + "    </div>\n"
                    + "</body>\n"
                    + "</html>";

            emailService.sendEmail(
                    entrenador.getCorreo(),
                    "Verificación de cuenta - TuAplicación",
                    html
            );

            return ResponseEntity.ok("Correo de verificación enviado correctamente a " + entrenador.getCorreo());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró un usuario con el correo proporcionado.");
    }

    @GetMapping("/verifyAccount")
    public ResponseEntity Verify(@RequestParam("token") String token) {

        if (verifyTokenService.validarToken(token)) {
            Optional<Entrenador> entrenador = entrenadorService.GetById(Long.valueOf(verifyTokenService.getUserIdbyToken(token)));
            if (entrenador.isPresent()) {
                Entrenador entreadorExist = entrenador.get();
                entrenadorService.Verify(Long.valueOf(entreadorExist.getIdEntrenador()));
                String html = """
                                              <!DOCTYPE html>
                                              <html lang="es">
                                              <head>
                                                  <meta charset="UTF-8">
                                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                  <title>Cuenta Validada</title>
                                                  <style>
                                                      body {
                                                          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                                          background-color: #f4f4f4;
                                                          margin: 0;
                                                          padding: 0;
                                                      }
                                                      .container {
                                                          max-width: 600px;
                                                          margin: 40px auto;
                                                          background-color: #ffffff;
                                                          border-radius: 10px;
                                                          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                                          overflow: hidden;
                                                      }
                                                      .header {
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          padding: 20px;
                                                          text-align: center;
                                                      }
                                                      .content {
                                                          padding: 30px;
                                                          color: #333333;
                                                          line-height: 1.6;
                                                      }
                                                      .button {
                                                          display: inline-block;
                                                          padding: 12px 24px;
                                                          margin: 20px 0;
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          text-decoration: none;
                                                          border-radius: 6px;
                                                          font-weight: bold;
                                                          transition: background-color 0.3s ease;
                                                      }
                                                      .button:hover {
                                                          background-color: #218838;
                                                      }
                                                      .footer {
                                                          text-align: center;
                                                          font-size: 12px;
                                                          color: #999999;
                                                          padding: 20px;
                                                          border-top: 1px solid #eeeeee;
                                                      }
                                                  </style>
                                              </head>
                                              <body>
                                                  <div class="container">
                                                      <div class="header">
                                                          <h1>Contrase\u00f1a Actualizada</h1>
                                                      </div>
                                                      <div class="content">
                                                          <p>\u00a1Hola   """ // Verde éxito
                        + entreadorExist.getUsername() + "!</p>\n"
                        + "            <p>Tu cuenta ha sido validada.</p>\n"
                        + "            <p>Si tú realizaste este cambio, no necesitas hacer nada más.</p>\n"
                        + "        </div>\n"
                        + "        <div class=\"footer\">\n"
                        + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                        + "        </div>\n"
                        + "    </div>\n"
                        + "</body>\n"
                        + "</html>";
                emailService.sendEmail(entreadorExist.getCorreo(), "Cuenta validada", html);
            }
        }

        return ResponseEntity.status(200).body("enviado correctamente");
    }

}
