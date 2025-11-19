package risosu.it.PokeApiClient.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import risosu.it.PokeApiClient.DAO.IEntrenadorRepository;
import risosu.it.PokeApiClient.DAO.IPokedexRepository;
import risosu.it.PokeApiClient.JPA.Entrenador;
import risosu.it.PokeApiClient.JPA.Pokedex;
import risosu.it.PokeApiClient.DAO.IPokedexEntrenadorRepository;
import risosu.it.PokeApiClient.DAO.IPokedexPokemonRepository;
import risosu.it.PokeApiClient.DAO.IPokemonRepository;
import risosu.it.PokeApiClient.DTO.PokeFavoritoDTO;
import risosu.it.PokeApiClient.DTO.EntrenadorDTO;
import risosu.it.PokeApiClient.DTO.FavoritoDTO;
import risosu.it.PokeApiClient.JPA.Audio;
import risosu.it.PokeApiClient.JPA.PokedexEntrenador;
import risosu.it.PokeApiClient.JPA.PokedexPokemon;
import risosu.it.PokeApiClient.JPA.PokedexPokemonId;
import risosu.it.PokeApiClient.JPA.Pokemon;
import risosu.it.PokeApiClient.JPA.Sprite;

@Service
public class EntrenadorService {

    private final IEntrenadorRepository iEntrenadorRepository;

    private final PasswordEncoder passwordEnconder;

    private final IPokedexRepository iPokedexRepository;

    private final IPokedexEntrenadorRepository iPokedexEntrenadorRepository;

    private final IPokedexPokemonRepository iPokedexPokemonRepository;

    private final IPokemonRepository iPokemonRepository;

    public EntrenadorService(IEntrenadorRepository iEntrenadorRepository, PasswordEncoder passwordEncoder,
            IPokedexRepository iPokedexRepository, IPokedexEntrenadorRepository iPokedexEntrenadorRepository,
            IPokedexPokemonRepository iPokedexPokemonRepository,
            IPokemonRepository iPokemonRepository) {
        this.iEntrenadorRepository = iEntrenadorRepository;
        this.passwordEnconder = passwordEncoder;
        this.iPokedexRepository = iPokedexRepository;
        this.iPokedexEntrenadorRepository = iPokedexEntrenadorRepository;
        this.iPokedexPokemonRepository = iPokedexPokemonRepository;
        this.iPokemonRepository = iPokemonRepository;
    }

    public List<Entrenador> GetAll() {
        List<Entrenador> entrenadores = iEntrenadorRepository.findAll();
        return entrenadores;
    }

    public Optional<Entrenador> GetById(Long idEntrenador) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findById(idEntrenador);
        if (entrenador.isPresent()) {
            return Optional.of(entrenador.get());
        } else {
            return Optional.empty();
        }
    }

    public Entrenador Add(Entrenador entrenador) {
        entrenador.setPassword(passwordEnconder.encode(entrenador.getPassword()));
        entrenador.setEstado(0);
        Entrenador newEntrenador = iEntrenadorRepository.save(entrenador);
        return newEntrenador;
    }

    public Entrenador Update(Entrenador entrenador) {
        try {
            // Buscar si existe el entrenador en la BD
            Optional<Entrenador> entrenadorBD = iEntrenadorRepository.findById(Long.valueOf(entrenador.getIdEntrenador()));

            if (entrenadorBD.isEmpty()) {
                // No se encontró el registro, lanzar excepción o manejarlo como null
                throw new RuntimeException("Entrenador no encontrado con ID: " + entrenador.getIdEntrenador());
            }

            // Actualizar los campos necesarios (opcional si el objeto ya viene completo)
            Entrenador updated = iEntrenadorRepository.save(entrenador);

            return updated;

        } catch (Exception e) {
            // Registrar el error en logs (buena práctica)
            System.err.println("❌ Error al actualizar el entrenador: " + e.getMessage());
            e.printStackTrace();

            // Puedes lanzar una excepción personalizada si quieres propagar el error
            throw new RuntimeException("Error al actualizar el entrenador", e);
        }
    }

    public Entrenador patchEntrenador(Long id, Entrenador cambios) {
        Optional<Entrenador> optional = iEntrenadorRepository.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("Entrenador no encontrado con ID: " + id);
        }

        Entrenador entrenador = optional.get();

        // Actualizamos solo los campos que vienen no nulos
        if (cambios.getNombre() != null && !cambios.getNombre().isEmpty()) {
            entrenador.setNombre(cambios.getNombre());
        }
        if (cambios.getApellidoPaterno() != null && !cambios.getApellidoPaterno().isEmpty()) {
            entrenador.setApellidoPaterno(cambios.getApellidoPaterno());
        }
        if (cambios.getApellidoMaterno() != null && !cambios.getApellidoMaterno().isEmpty()) {
            entrenador.setApellidoMaterno(cambios.getApellidoMaterno());
        }
        if (cambios.getSexo() != null && !cambios.getSexo().isEmpty()) {
            entrenador.setSexo(cambios.getSexo());
        }
        if (cambios.getCorreo() != null && !cambios.getCorreo().isEmpty()) {
            entrenador.setCorreo(cambios.getCorreo());
        }
        if (cambios.getPassword() != null && !cambios.getPassword().isEmpty()) {
            entrenador.setPassword(cambios.getPassword());
        }
        if (cambios.getRol() != null) {
            entrenador.setRol(cambios.getRol());
        }
        if (cambios.getEstado() == 1) { // si 0 no es válido como valor de cambio
            entrenador.setEstado(entrenador.getEstado() == 1 ? 0 : 1);
        }

        return iEntrenadorRepository.save(entrenador);
    }

    public Optional<Entrenador> Delete(Long idEntrenador) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findById(idEntrenador);
        iEntrenadorRepository.delete(entrenador.get());
        if (entrenador.isPresent()) {
            return Optional.of(entrenador.get());
        } else {
            return Optional.empty();
        }
    }

    public UserDetails loadEntrenadorByUsername(String username) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findByUsername(username);
        if (entrenador.isPresent()) {
            return (UserDetails) entrenador.get();
        } else {
            return null;
        }

    }

    public Entrenador loadByCorreo(String correo) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findByCorreo(correo);
        if (entrenador.isPresent()) {
            return entrenador.get();
        }
        return null;

    }

    public Entrenador Verify(Long idEntrenador) {
        Optional<Entrenador> entrenador = iEntrenadorRepository.findById(idEntrenador);
        if (entrenador.isPresent()) {
            entrenador.get().setEstado(1);
            entrenador.get().setVerify(1);
            Entrenador newEntrenador = iEntrenadorRepository.save(entrenador.get());
            return newEntrenador;
        } else {
            return null;
        }
    }

    public int Count() {
        return (int) iEntrenadorRepository.count();
    }

    public Pokemon AddPokemon(PokeFavoritoDTO pokemon) {

        Pokemon pokemonAguardar = new Pokemon();
        pokemonAguardar.setNombre(pokemon.getNombre());
        pokemonAguardar.setAltura(pokemon.getAltura());
        pokemonAguardar.setAncho(pokemon.getPeso());

        Sprite foto = new Sprite();
        foto.setDefaultfrontal(pokemon.getImg());

        Audio audio = new Audio();
        audio.setActual(pokemon.getCrie());

        pokemonAguardar.setSprite(foto);

        pokemonAguardar.setAudio(audio);

        pokemonAguardar.setIdJson(pokemon.getIdPokemon());

        iPokemonRepository.saveAndFlush(pokemonAguardar);

        return pokemonAguardar;

    }

    public boolean AddFavorites(String user, Long pokeId, Boolean status, PokeFavoritoDTO pokemon) {

        //Este objeto representa si un entrenador tiene asignada una pokedex:
        PokedexEntrenador pokedexDeUsuario = new PokedexEntrenador();

        //Este objeto reporesenta una pokedex:
        Pokedex pokedexNew = new Pokedex();

        //Este objeto representa la relacion entre una pokedex y un pokemon(Favorito)
        PokedexPokemon pokeFavorito = new PokedexPokemon();

        List<FavoritoDTO> favoritosDTO = new ArrayList<>();
        //Buscamos el id del pokemon, para ver si ya esta registrado
        Optional<Pokemon> existente = iPokemonRepository.findByIdJson(pokeId.intValue());

        //Debemos crear un objeto de tipo pokemon para almacenar sus datos en caso de que no exista:
        Pokemon pokeSave = new Pokemon();

        //Esta es la lista de pokemons:
        List<PokedexPokemon> favoritos = new ArrayList<>();

        //id de pokedex, ya que el id puede ya existir, o no:
        int idPokedexEntrenador;

        //Si el pokemon no esta registrado, registrarlo primero:
        if (existente.isEmpty()) {
            pokeSave = AddPokemon(pokemon);
        } else {
            //Si el pokeAnimal ya estaba registrado, ya no lo volvemos a registrar:
            pokeSave = existente.get();
        }

        //Necesitamos saber a que entrenador se le asiganara el pokemon como favorito, usamos su username para averiguarlo:
        Optional<Entrenador> entrenador = iEntrenadorRepository.findByUsername(user);

        if (entrenador.isPresent()) {
            Entrenador entrenador2 = entrenador.get();

            int idEntrenador = entrenador2.getIdEntrenador();
            String nombre = entrenador2.getNombre();

            //Averiguamos si el usuario ya tiene una pokedex asignada, usando su id: 
            List<PokedexEntrenador> entrenadorPokemon = iPokedexEntrenadorRepository.findByIdEntrenador(idEntrenador);

            //Si el usuario no tiene una pokedex asignada:
            if (entrenadorPokemon.isEmpty()) {

                // crear una pokedex nueva al usuario
                pokedexNew.setNombre("¡Tu Pokedex!" + nombre);
                iPokedexRepository.saveAndFlush(pokedexNew);

                //Asignarle la pokedex al usuario:
                pokedexDeUsuario.setIdEntrenador(idEntrenador);
                pokedexDeUsuario.setIdPokedex(pokedexNew.getIdPokedex());

                iPokedexEntrenadorRepository.save(pokedexDeUsuario);

                idPokedexEntrenador = pokedexNew.getIdPokedex();
                //Establecemos la id de la nueva pokedex creada:
                pokeFavorito.setIdPokedex(idPokedexEntrenador);
            } else {

                //Si ya tiene una pokedex asignada, usamos esa id para la relacion:
                idPokedexEntrenador = entrenadorPokemon.get(0).getIdPokedex();
                pokeFavorito.setIdPokedex(idPokedexEntrenador);

            }

        }
        //asignar el pokemon favorito del usuario a su pokedex:
        pokeFavorito.setIdPokemon(pokeSave.getIdPokemon());

        try {
            iPokedexPokemonRepository.save(pokeFavorito);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<FavoritoDTO> GetFavorites(String user) {

        //Este objeto representa la relacion entre una pokedex y un pokemon(Favorito)
        PokedexPokemon pokeFavorito = new PokedexPokemon();

        //Aqui lmacenaremos la lista de pokemons favoritos del usuario:
        List<FavoritoDTO> favoritosDTO = new ArrayList<>();

        //Esta es la lista que representa a los pokemons favoritos:
        List<PokedexPokemon> favoritos = new ArrayList<>();

        //Obtenemos el id del Usuario por medio de su username:
        Optional<Entrenador> entrenador = iEntrenadorRepository.findByUsername(user);
        Entrenador entrenador2 = entrenador.get();

        int idEntrenador = entrenador2.getIdEntrenador();

        //Averiguamos si el usuario ya tiene una pokedex asignada, usando su id: 
        List<PokedexEntrenador> entrenadorPokemon = iPokedexEntrenadorRepository.findByIdEntrenador(idEntrenador);
        int idPokedexEntrenador;

        //Si no tiene pokedex :
        if (entrenadorPokemon.isEmpty()) {

        } else {

            idPokedexEntrenador = entrenadorPokemon.get(0).getIdPokedex();
            pokeFavorito.setIdPokedex(idPokedexEntrenador);

            //retornar el objeto que contiene a los pokeFavoritos:
            favoritos = iPokedexPokemonRepository.findByIdPokedex(idPokedexEntrenador);

            //Enviar el idJson del pokemon y no el id de la persistencia:
            for (PokedexPokemon favorito : favoritos) {

                Integer idPokemon = favorito.getIdPokemon();

                Optional<Pokemon> poke = iPokemonRepository.findById(idPokemon.longValue());
                if (poke.isPresent()) {

                    int idJson = poke.get().getIdJson();
                    favoritosDTO.add(new FavoritoDTO(favorito.getIdPokedex(), idJson));
                }
            }
        }
        return favoritosDTO;

    }

    public List<PokedexEntrenador> GetFavorites(int idEntrenador) {
        List<PokedexEntrenador> pokemonesFavs
                = iPokedexEntrenadorRepository.cargaPokedexCompleta(idEntrenador);
        return pokemonesFavs;
    }

    public boolean deleteFavorite(String username, Long pokeId) {

        // 1. Buscar al entrenador por username
        Optional<Entrenador> entrenadorOpt = iEntrenadorRepository.findByUsername(username);
        if (entrenadorOpt.isEmpty()) {
            return false;
        }

        Entrenador entrenador = entrenadorOpt.get();        int idEntrenador = entrenador.getIdEntrenador();

        // 2. Buscar la(s) pokedex del entrenador
        List<PokedexEntrenador> pokedexUser
                = iPokedexEntrenadorRepository.findByIdEntrenador(idEntrenador);

        if (pokedexUser.isEmpty()) {
            return false;
        }

        // decidir cuál pokédex usar:
        // Supongamos que toma la primera por simplicidad:
        int idPokedex = pokedexUser.get(0).getIdPokedex();
        
        
        //Buscar al pokemon por su idJson y obtener el id de BD:
       Optional<Pokemon> pokeObj =  iPokemonRepository.findByIdJson(pokeId.intValue());
        Pokemon poke = pokeObj.get();
        int idPokemon = poke.getIdPokemon();
        

        //  Crear la clave compuesta
        PokedexPokemonId pk = new PokedexPokemonId(idPokedex, idPokemon);

        // Eliminar usando deleteById
        if (!iPokedexPokemonRepository.existsById(pk)) {
            return false; // no existe, no se puede eliminar
        }

        try {
            iPokedexPokemonRepository.deleteById(pk);
        } catch (Exception e) {
            System.out.println("error:" +  e.getLocalizedMessage());
           
        }
        return true;
    }
}
