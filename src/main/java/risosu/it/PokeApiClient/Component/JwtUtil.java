package risosu.it.PokeApiClient.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey key
            = Keys.hmacShaKeyFor("f9Jr4L1x!aP$6mQz7VkB#9cD2hE*3nTqU@8yZrW%5oXj&1bH+KfG^0sLpMvNtY".getBytes());

    public String generateToken(String username, String role) {
        String jti = UUID.randomUUID().toString();

        return Jwts.builder().setSubject(username).claim("role", role)
                .setId(jti).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .signWith(key).compact();
    }

    public Jws<Claims> validateToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            throw new ExpiredJwtException(claimsJws.getHeader(), claims, "Token has expired");
        }
        return claimsJws;
    }
}
