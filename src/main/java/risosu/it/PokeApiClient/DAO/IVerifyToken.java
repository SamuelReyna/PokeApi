package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import risosu.it.PokeApiClient.JPA.VerifyToken;

public interface IVerifyToken extends JpaRepository<VerifyToken, Object> {

    void deleteByUserId(int userId);

    Optional<VerifyToken> findByToken(String token);
}
