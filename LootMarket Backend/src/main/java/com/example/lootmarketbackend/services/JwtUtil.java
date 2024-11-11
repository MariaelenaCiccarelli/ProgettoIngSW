package com.example.lootmarketbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtil {

    // Chiave segreta usata per la firma
    private static final String SECRET_KEY = "secretMagnificoBellissmoDelMondoCheVerraGiuroSuMioZio";

    public static Claims decodeJWT(String jwt) {
        try {
            // Decodifica e parsing del token
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Token non valido o firma non valida.");
        }
    }
}