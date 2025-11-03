package risosu.it.PokeApiClient.Configuration;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class baseUrl {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        int maxInMemorySize = 10 * 1024 * 1024; // 10 MB

        return builder
                .baseUrl("https://pokeapi.co/api/v2")
                .codecs(configurer
                        -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySize))
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(30))))
                .build();
    }
}
