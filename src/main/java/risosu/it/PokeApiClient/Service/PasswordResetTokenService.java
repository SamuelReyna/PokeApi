package risosu.it.PokeApiClient.Service;

import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;
import risosu.it.PokeApiClient.DAO.IPasswordResetToken;
import risosu.it.PokeApiClient.JPA.PasswordResetToken;

@Service
public class PasswordResetTokenService {

    private final IPasswordResetToken iPasswordResetToken;

    public PasswordResetTokenService(IPasswordResetToken iPasswordResetToken) {
        this.iPasswordResetToken = iPasswordResetToken;
    }

    @Transactional
    public String GenerateToken(int idEntrenador) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(3);

        PasswordResetToken verifyToken = new PasswordResetToken();
        verifyToken.setExpDateTime(expiration);
        verifyToken.setToken(token);
        verifyToken.setUserId(idEntrenador);

        iPasswordResetToken.save(verifyToken);

        return token;
    }

    public boolean validarToken(String token) {
        return iPasswordResetToken.findByToken(token)
                .filter(t -> t.getExpDateTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public int getUserIdbyToken(String token) {
        return iPasswordResetToken.findByToken(token)
                .filter(t -> t.getExpDateTime().isAfter(LocalDateTime.now()))
                .map(PasswordResetToken::getUserId)
                .orElse(null);
    }

    public void eliminarToken(String token) {
        iPasswordResetToken.findByToken(token).ifPresent(iPasswordResetToken::delete);
    }
}
