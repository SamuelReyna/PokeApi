package risosu.it.PokeApiClient.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import risosu.it.PokeApiClient.JPA.PasswordResetToken;

@Repository
public interface IPasswordResetToken extends JpaRepository<PasswordResetToken, Object> {

    void deleteByUserId(int userId);

    Optional<PasswordResetToken> findByToken(String token);
}
