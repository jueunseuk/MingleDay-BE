package returns.mingleday.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import returns.mingleday.domain.users.User;

import java.security.Key;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.jwt-key}")
    private String jwtSecretKey;

    @Value("${jwt.access-token-expiration}")
    private Long tokenExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    public boolean isValidToken(String token) {
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        return true;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public User getUser(String token) {
        return new User();
    }
}
