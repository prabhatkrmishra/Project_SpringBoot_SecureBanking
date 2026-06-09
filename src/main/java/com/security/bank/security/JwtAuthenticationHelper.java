package com.security.bank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for JWT token generation, validation, and parsing.
 */
@Component
public class JwtAuthenticationHelper {

    private static final String SECRET = "banksecurityapplicationjwtsecretkeybanksecurityapplication";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // 5 hours

    /**
     * Generates a JWT token for the given user.
     *
     * @param userDetails the authenticated user
     * @return JWT string
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)).signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Extracts username from token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Extracts expiration date from token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Generic claim extraction.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the token has expired.
     */
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * Validates the token against the user details.
     *
     * @return true if token is not expired and username matches
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}