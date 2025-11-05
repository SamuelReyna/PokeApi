package risosu.it.PokeApiClient.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import risosu.it.PokeApiClient.Service.EntrenadorService;

@Component
public class JwtFilter extends GenericFilter {

    private final JwtUtil jwtUtil;
    private final EntrenadorService entrenadorService;

    public JwtFilter(JwtUtil jwtUtil, EntrenadorService entrenadorService) {
        this.jwtUtil = jwtUtil;
        this.entrenadorService = entrenadorService;

    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletRequest response = (HttpServletRequest) sr1;

        String header = request.getHeader("Authorization");
        String path = request.getRequestURI();

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Jws<Claims> claims = jwtUtil.validateToken(token);
                UserDetails userDetails
                        = entrenadorService.loadEntrenadorByUsername(claims.getBody().getSubject());

                UsernamePasswordAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (UsernameNotFoundException e) {
            }
        }
        fc.doFilter(sr, sr1);

    }

    @Override
    public void destroy() {
        super.destroy(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
