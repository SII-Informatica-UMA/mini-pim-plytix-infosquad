package minipimplytixinfosquad.entidades.security;


import minipimplytixinfosquad.entidades.entities.Cuenta;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getUsernameFromToken(token));
    }

    //LO HE CAMBIADO
    //extrae el claim "role" del token JWT, lo convierte a string
    //y lo devuelve dentro de un Optional<String> para manejar el 
    //caso en el que no exista o falle
    public Optional<String> getRoleFromToken(String token) {
        try {
            return Optional.ofNullable(getAllClaimsFromToken(token).get("role"))
                .map(Object::toString);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //ESTE MICROSERVICIO NO GENERA TOKENS, ESO LO HACE USUARIOS
    /* 
    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", usuario.getRole());
        return doGenerateToken(claims, usuario.getId().toString());
    }
        

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
            .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    */

}