package risosu.it.PokeApiClient.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request
                = (HttpServletRequest) req;
        HttpServletResponse response
                = (HttpServletResponse) res;
        String header = request.getHeader("Authorization");

        String path = request.getRequestURI();

        // ❌ Ignorar las rutas públicas
        if (path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Jws<Claims> claims = jwtUtil.validateToken(token);

                UserDetails userDetails
                        = entrenadorService.loadEntrenadorByUsername(claims.getBody().getSubject());

                UsernamePasswordAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (UsernameNotFoundException e) {
            }
        }

        chain.doFilter(req, res);

    }

    @Override
    public void destroy() {
        super.destroy(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
