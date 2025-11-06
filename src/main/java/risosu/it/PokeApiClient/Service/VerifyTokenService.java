package risosu.it.PokeApiClient.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;
import risosu.it.PokeApiClient.DAO.IVerifyToken;
import risosu.it.PokeApiClient.JPA.VerifyToken;

@Service
public class VerifyTokenService {

    public VerifyTokenService(risosu.it.PokeApiClient.DAO.IVerifyToken iVerifyToken) {
        this.iVerifyToken = iVerifyToken;
    }
    private final IVerifyToken iVerifyToken;

    public String GenerateToken(int userId) {
        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(3);

        VerifyToken resetToken = new VerifyToken();
        resetToken.setExpDate(expiration);
        resetToken.setToken(token);
        resetToken.setIduser(userId);

        iVerifyToken.save(resetToken);
        return token;
    }

    public boolean validarToken(String token) {
        return iVerifyToken.findByToken(token)
                .filter(t -> t.getExpDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public int getUserIdbyToken(String token) {
        return iVerifyToken.findByToken(token)
                .filter(t -> t.getExpDate().isAfter(LocalDateTime.now()))
                .map(VerifyToken::getIduser)
                .orElse(null);
    }

    public void eliminarToken(String token) {
        iVerifyToken.findByToken(token).ifPresent(iVerifyToken::delete);
    }
}
