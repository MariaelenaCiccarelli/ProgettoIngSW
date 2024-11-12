package com.example.lootmarketbackend.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

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
            return null;
        }catch (ExpiredJwtException e){
            throw e;
        }
    }

    public static String encodeJWT(String mail) {
        String jwsToken;
        try {

            jwsToken = Jwts.builder()
                    .setIssuer("LootMarket")
                    .claim("mail", mail)
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plusSeconds(120)))
                    .signWith(
                            SignatureAlgorithm.HS256,
                            "secretMagnificoBellissmoDelMondoCheVerraGiuroSuMioZio".getBytes("UTF-8")
                    )
                    .compact();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return jwsToken;
    }
}
