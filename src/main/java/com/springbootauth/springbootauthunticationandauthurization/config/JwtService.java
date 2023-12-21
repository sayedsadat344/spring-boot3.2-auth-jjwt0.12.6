package com.springbootauth.springbootauthunticationandauthurization.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "7191d19361fdc0cb2f57bfdaa768c2018625abee92033bd9b66379c5f5f9b594";
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract claims from the token
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getPublicSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .signKe(getPublicSigningKey())
//                .parseClaimsJws(token)
//                .getBody();
//    }

    private Key getPublicSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
         return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername()).
                issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(getPublicSigningKey()).compact();
    }

    public boolean validateToken(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !istokenExpired(token));
    }

    private boolean istokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
