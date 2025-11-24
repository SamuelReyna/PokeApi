package risosu.it.PokeApiClient.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import risosu.it.PokeApiClient.DTO.PokeFavoritoDTO;
import risosu.it.PokeApiClient.DTO.EntrenadorDTO;
import risosu.it.PokeApiClient.DTO.Password;
import risosu.it.PokeApiClient.Service.EntrenadorService;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.Service.EmailService;
import risosu.it.PokeApiClient.JPA.Rol;

@RestController
@RequestMapping("api/entrenador")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    private final EmailService emailService;

    public EntrenadorController(EntrenadorService entrenadorService, EmailService emailService) {
        this.emailService = emailService;
        this.entrenadorService = entrenadorService;
    }

    @GetMapping()
    public ResponseEntity GetAll() {
        return ResponseEntity.ok(entrenadorService.GetAll());
    }

    @GetMapping("/{idEntrenador}")
    public ResponseEntity GetOne(@PathVariable("idEntrenador") Long idEntrenador) {
        return ResponseEntity.ok(entrenadorService.GetById(idEntrenador));
    }

    @PostMapping()
    public ResponseEntity Add(@RequestBody EntrenadorDTO entrenador) {
        Entrenador entrenadorBD = new Entrenador();
        entrenadorBD.setNombre(entrenador.getNombre());
        entrenadorBD.setApellidoPaterno(entrenador.getApellidoPaterno());
        entrenadorBD.setApellidoMaterno(entrenador.getApellidoMaterno());
        entrenadorBD.setCorreo(entrenador.getCorreo());
        entrenadorBD.setSexo(entrenador.getSexo());
        entrenadorBD.setPassword(entrenador.getPassword());
        entrenadorBD.setUsername(entrenador.getUsername());
        Rol rol = new Rol();
        rol.setIdrol(entrenador.rol.getIdRol());
        entrenadorBD.setRol(rol);
        return ResponseEntity.ok(entrenadorService.Add(entrenadorBD));
    }

    @GetMapping("/{username}/username")
    public ResponseEntity GetOne(@PathVariable("username") String username) {
        return ResponseEntity.ok(entrenadorService.loadEntrenadorByUsername(username));
    }

    @GetMapping("/count")
    public ResponseEntity Count() {
        return ResponseEntity.ok(entrenadorService.Count());
    }

    @PatchMapping("/{idEntrenador}")
    public ResponseEntity Patch(@PathVariable("idEntrenador") Long idEntrenador, @RequestBody EntrenadorDTO entrenador) {
        Entrenador entrenadorBD = new Entrenador();
        entrenadorBD.setNombre(entrenador.getNombre());
        entrenadorBD.setApellidoPaterno(entrenador.getApellidoPaterno());
        entrenadorBD.setApellidoMaterno(entrenador.getApellidoMaterno());
        entrenadorBD.setCorreo(entrenador.getCorreo());
        entrenadorBD.setSexo(entrenador.getSexo());
        entrenadorBD.setPassword(entrenador.getPassword());
        entrenadorBD.setUsername(entrenador.getUsername());
        Rol rol = new Rol();
        rol.setIdrol(entrenador.rol.getIdRol());
        entrenadorBD.setRol(rol);
        Optional<Entrenador> entrenadorOld = entrenadorService.GetById(idEntrenador);
        if (entrenadorOld.isPresent()) {
            Entrenador entrenadorOld2 = entrenadorOld.get();
            if (!Objects.equals(entrenador.getUsername(), entrenadorOld2.getUsername())) {
                String html = """
                                          <!DOCTYPE html>
                                          <html lang="es">
                                          <head>
                                              <meta charset="UTF-8">
                                              <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                              <title>Usuario Actualizado - Centro Pok\u00e9mon</title>
                                              <style>
                                                  body {
                                                      font-family: 'Press Start 2P', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                                      background-color: #b7f57f;
                                                      background-image: linear-gradient(135deg, #b7f57f 40%, #3b4cca 40%);
                                                      margin: 0;
                                                      padding: 0;
                                                  }
                                                  .container {
                                                      max-width: 600px;
                                                      margin: 50px auto;
                                                      background-color: #ffffff;
                                                      border: 5px solid #3b4cca;
                                                      border-radius: 16px;
                                                      overflow: hidden;
                                                      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
                                                  }
                                                  .header {
                                                      background-color: #4CAF50;
                                                      color: #ffffff;
                                                      text-align: center;
                                                      padding: 20px;
                                                  }
                                                  .header h1 {
                                                      margin: 0;
                                                      font-size: 22px;
                                                  }
                                                  .content {
                                                      padding: 30px;
                                                      text-align: center;
                                                      color: #333;
                                                  }
                                                  .pokeball {
                                                      width: 80px;
                                                      height: 80px;
                                                      margin: 0 auto 20px auto;
                                                      background: radial-gradient(circle at 50% 45%, #fff 40%, #000 41%, #000 44%, #4CAF50 45%);
                                                      border-radius: 50%;
                                                      border: 4px solid #000;
                                                      position: relative;
                                                  }
                                                  .pokeball::after {
                                                      content: '';
                                                      position: absolute;
                                                      top: 35%;
                                                      left: 35%;
                                                      width: 30%;
                                                      height: 30%;
                                                      background: white;
                                                      border: 4px solid black;
                                                      border-radius: 50%;
                                                  }
                                                  .button {
                                                      display: inline-block;
                                                      padding: 14px 30px;
                                                      background-color: #4CAF50;
                                                      color: #fff;
                                                      text-decoration: none;
                                                      border-radius: 8px;
                                                      font-weight: bold;
                                                      box-shadow: 0 4px #2e7d32;
                                                      transition: all 0.3s ease;
                                                  }
                                                  .button:hover {
                                                      background-color: #43a047;
                                                      transform: scale(1.05);
                                                  }
                                                  .footer {
                                                      background-color: #f7f7f7;
                                                      padding: 15px;
                                                      text-align: center;
                                                      font-size: 12px;
                                                      color: #777;
                                                      border-top: 1px solid #eee;
                                                  }
                                              </style>
                                          </head>
                                          <body>
                                              <div class="container">
                                                  <div class="header">
                                                      <h1>Usuario Actualizado \u2705</h1>
                                                  </div>
                                                  <div class="content">
                                                      <div class="pokeball"></div>
                                                      <h2>\u00a1Hola """ + entrenadorBD.getUsername() + "!</h2>\n"
                        + "            <p>Queremos informarte que tu usuario ha sido cambiado exitosamente.</p>\n"
                        + "            <p>Si tú realizaste este cambio, no necesitas hacer nada más.</p>\n"
                        + "            <p>Si <strong>no fuiste tú</strong>, te recomendamos  contactar con el soporte técnico.</p>\n"
                        + "            <a href=\"mailto:soporte@pokemoncenter.com\" class=\"button\">Contactar Soporte</a>\n"
                        + "        </div>\n"
                        + "        <div class=\"footer\">\n"
                        + "            © 2025 Centro Pokémon | ¡Atrápalos a todos!\n"
                        + "        </div>\n"
                        + "    </div>\n"
                        + "</body>\n"
                        + "</html>";

                emailService.sendEmail(entrenadorBD.getCorreo(), "Actualizacion de nombre de usuario", html);

            }

        }
        Entrenador entrenadorModifed = entrenadorService.patchEntrenador(idEntrenador, entrenadorBD);

        return ResponseEntity.ok(entrenadorModifed);
    }

    @PatchMapping("/{idEntrenador}/password")
    public ResponseEntity ChangePassword(@PathVariable("idEntrenador") Long idEntrenador, @RequestBody Password password) {
        Entrenador entrenador = new Entrenador();
        if (password.getPassword().equals(password.getConfirmPassword())) {
            entrenador.setPassword(password.getPassword());
        }
        Entrenador entrenadorModifed = entrenadorService.patchEntrenador(idEntrenador, entrenador);
        String html = """
                                          <!DOCTYPE html>
                                          <html lang="es">
                                          <head>
                                              <meta charset="UTF-8">
                                              <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                              <title>Contrase\u00f1a Actualizada - Centro Pok\u00e9mon</title>
                                              <style>
                                                  body {
                                                      font-family: 'Press Start 2P', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                                      background-color: #b7f57f;
                                                      background-image: linear-gradient(135deg, #b7f57f 40%, #3b4cca 40%);
                                                      margin: 0;
                                                      padding: 0;
                                                  }
                                                  .container {
                                                      max-width: 600px;
                                                      margin: 50px auto;
                                                      background-color: #ffffff;
                                                      border: 5px solid #3b4cca;
                                                      border-radius: 16px;
                                                      overflow: hidden;
                                                      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
                                                  }
                                                  .header {
                                                      background-color: #4CAF50;
                                                      color: #ffffff;
                                                      text-align: center;
                                                      padding: 20px;
                                                  }
                                                  .header h1 {
                                                      margin: 0;
                                                      font-size: 22px;
                                                  }
                                                  .content {
                                                      padding: 30px;
                                                      text-align: center;
                                                      color: #333;
                                                  }
                                                  .pokeball {
                                                      width: 80px;
                                                      height: 80px;
                                                      margin: 0 auto 20px auto;
                                                      background: radial-gradient(circle at 50% 45%, #fff 40%, #000 41%, #000 44%, #4CAF50 45%);
                                                      border-radius: 50%;
                                                      border: 4px solid #000;
                                                      position: relative;
                                                  }
                                                  .pokeball::after {
                                                      content: '';
                                                      position: absolute;
                                                      top: 35%;
                                                      left: 35%;
                                                      width: 30%;
                                                      height: 30%;
                                                      background: white;
                                                      border: 4px solid black;
                                                      border-radius: 50%;
                                                  }
                                                  .button {
                                                      display: inline-block;
                                                      padding: 14px 30px;
                                                      background-color: #4CAF50;
                                                      color: #fff;
                                                      text-decoration: none;
                                                      border-radius: 8px;
                                                      font-weight: bold;
                                                      box-shadow: 0 4px #2e7d32;
                                                      transition: all 0.3s ease;
                                                  }
                                                  .button:hover {
                                                      background-color: #43a047;
                                                      transform: scale(1.05);
                                                  }
                                                  .footer {
                                                      background-color: #f7f7f7;
                                                      padding: 15px;
                                                      text-align: center;
                                                      font-size: 12px;
                                                      color: #777;
                                                      border-top: 1px solid #eee;
                                                  }
                                              </style>
                                          </head>
                                          <body>
                                              <div class="container">
                                                  <div class="header">
                                                      <h1>Contrase\u00f1a Actualizada \u2705</h1>
                                                  </div>
                                                  <div class="content">
                                                      <div class="pokeball"></div>
                                                      <h2>\u00a1Hola """ + entrenadorModifed.getUsername() + "!</h2>\n"
                + "            <p>Queremos informarte que tu contraseña ha sido cambiada exitosamente.</p>\n"
                + "            <p>Si tú realizaste este cambio, no necesitas hacer nada más.</p>\n"
                + "            <p>Si <strong>no fuiste tú</strong>, te recomendamos restablecer tu contraseña de inmediato o contactar con el soporte técnico.</p>\n"
                + "            <a href=\"mailto:soporte@pokemoncenter.com\" class=\"button\">Contactar Soporte</a>\n"
                + "        </div>\n"
                + "        <div class=\"footer\">\n"
                + "            © 2025 Centro Pokémon | ¡Atrápalos a todos!\n"
                + "        </div>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";

        emailService.sendEmail(entrenadorModifed.getCorreo(), "Actualizacion de contraseña", html);
        return ResponseEntity.ok(entrenadorModifed);
    }

    @PatchMapping("/{idEntrenador}/estado")
    public ResponseEntity BajaLogica(@PathVariable("idEntrenador") Long idEntrenador) {
        Entrenador entrenador = new Entrenador();
        entrenador.setEstado(1);
        return ResponseEntity.ok(entrenadorService.patchEntrenador(idEntrenador, entrenador));
    }

    @DeleteMapping("/{idEntrenador}")
    public ResponseEntity Delete(@PathVariable("idEntrenador") int idEntrenador) {
        return ResponseEntity.ok(entrenadorService.Delete(Long.valueOf(idEntrenador)));
    }

    @PostMapping("/{user}")
    public ResponseEntity AddFavorites(@RequestBody PokeFavoritoDTO pokemon, @PathVariable String user) {

        Long idPokemon = pokemon.getIdPokemon().longValue();
        Boolean favorito = pokemon.getFavorito();

        return ResponseEntity.ok(entrenadorService.AddFavorites(user, idPokemon, favorito, pokemon));
    }

    @GetMapping("getFavorites/{user}")
    public ResponseEntity GetFavorites(@PathVariable String user) {
        return ResponseEntity.ok(entrenadorService.GetFavorites(user));
    }

    @PostMapping("delete/{user}")
    public ResponseEntity DeleteFavorites(@RequestBody PokeFavoritoDTO pokemon, @PathVariable String user) {

        Long idPokemon = pokemon.getIdPokemon().longValue();

        return ResponseEntity.ok(entrenadorService.deleteFavorite(user, idPokemon));
    }

    @GetMapping("/favorites/{idEntrenador}")
    public ResponseEntity GetFavorites(@PathVariable("idEntrenador") int idEntrenador) {
        return ResponseEntity.ok(entrenadorService.GetFavorites(idEntrenador));
    }

}
