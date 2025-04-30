package minipimplytixinfosquad.entidades.security;

import minipimplytixinfosquad.entidades.entities.Cuenta;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT Token viene en la forma de "Bearer token". Eliminamos el término Bearer del inicio
        // para obtener solo el token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("No puedo obtener el JWT");
            } catch (ExpiredJwtException e) {
                logger.info("El token ha expirado");
            }
            logger.info("usuario = " + username);
        } else {
            logger.info("El token no comienza con Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            /* 
            //LO HE CAMBIADO
            var authorities = jwtTokenUtil.getRoleFromToken(jwtToken)
                .map(SimpleGrantedAuthority::new)
                .stream().collect(Collectors.toSet());
            */
            /* 
            var authorities = jwtTokenUtil.getRoleFromToken(jwtToken)
                .map(role -> {
                    // Traducción segura de ADMINISTRADOR → ADMIN
                    if ("ADMINISTRADOR".equalsIgnoreCase(role)) return "ADMIN";
                    return role;
                })
                .map(SimpleGrantedAuthority::new)
                .map(Collections::singleton)
                .orElse(Collections.emptySet());

                UserDetails userDetails = new User(username, "", authorities);
            */

            /* 
            var authority = jwtTokenUtil.getRoleFromToken(jwtToken)
                .map(role -> {
                    if ("ADMINISTRADOR".equalsIgnoreCase(role)) return "ADMIN";
                    return role;
                })
                .map(SimpleGrantedAuthority::new)
                .orElse(null);
            */
            var authority = jwtTokenUtil.getRoleFromToken(jwtToken)
                .map(role -> {
                    if ("ADMINISTRADOR".equalsIgnoreCase(role)) return "ROLE_ADMIN";
                    return "ROLE_" + role.toUpperCase();
                })
                .map(SimpleGrantedAuthority::new)
                .orElse(null);


            if (authority != null) {
                UserDetails userDetails = new User(username, "", Collections.singleton(authority));

                if (!jwtTokenUtil.isTokenExpired(jwtToken)) {
                    
                    logger.info("Rol original del token: " + jwtTokenUtil.getRoleFromToken(jwtToken).orElse("NO_ROLE"));
                    logger.info("Rol extraído del token (tras traducción): " + authority.getAuthority());
                    logger.info("Authorities del user: " + userDetails.getAuthorities());
            
                    Long idUsuario = jwtTokenUtil.getUserIdFromToken(jwtToken); // <- este método debes tenerlo en JwtUtil
                    request.setAttribute("idUsuario", idUsuario); // <-- ESTA LÍNEA ES LA CLAVE

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                        );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    logger.debug("Token no válido");
                }
            }

            /* 
            if (!jwtTokenUtil.isTokenExpired(jwtToken)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                logger.debug("usernamePasswordAuthenticationToken = " + usernamePasswordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                logger.debug("Token no válido");
            }

            */
        }
        // A la ida

        chain.doFilter(request, response);

        // A la vuelta
    }

}
