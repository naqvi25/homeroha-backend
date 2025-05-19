    package com.homeroha.security;

    import io.jsonwebtoken.*;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;

    import javax.crypto.SecretKey;
    import java.util.Date;

    @Component
    public class JwtTokenProvider {

        private static final long EXPIRATION_TIME = 86400000; // 1 day
        private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Or load from env

        public String generateToken(String email) {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(secretKey)
                    .compact();
        }

        public String getEmailFromToken(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        public boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = getEmailFromToken(token);
            return (username != null && username.equals(userDetails.getUsername()));
        }

    }
