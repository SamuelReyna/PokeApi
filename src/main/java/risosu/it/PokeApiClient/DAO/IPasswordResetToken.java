package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import risosu.it.PokeApiClient.JPA.PasswordResetToken;

public interface IPasswordResetToken extends JpaRepository<PasswordResetToken, Object> {

    void deleteByUserId(int userId);

    Optional<PasswordResetToken> findByToken(String token);
}
