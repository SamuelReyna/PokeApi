package risosu.it.PokeApiClient;

import risosu.it.PokeApiClient.Configuration.baseUrl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import risosu.it.PokeApiClient.Service.GetAllPokemonsService;

@SpringBootApplication
@EnableAsync
public class PokeApiClientApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PokeApiClientApplication.class, args);
        var ctx = new AnnotationConfigApplicationContext(baseUrl.class);
        var service = ctx.getBean(GetAllPokemonsService.class);

        service.persistirPokemones(); // 🔥 descarga y guarda todo

        ctx.close();
    }
}
