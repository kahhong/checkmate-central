package com.ila.checkmatecentral.utility;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtUtil {
    private static final SecretKey key = Jwts.SIG.HS256.key().build();
    private static final String SECRET = Base64.getEncoder().encodeToString(key.getEncoded());
    
    @Value("${jwt.lifetime-in-minutes}")
    private static final int LIFETIME_IN_MINUTES = 720;

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public static String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .issuer("ILA")
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(LIFETIME_IN_MINUTES * 60L)))
            .signWith(getSigningKey())
            .compact();
    }

    private static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
