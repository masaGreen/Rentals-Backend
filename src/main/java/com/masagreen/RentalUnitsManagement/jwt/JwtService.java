package com.masagreen.RentalUnitsManagement.jwt;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtService {

     @Value("${jwt.secret}")
     private String secret;
    
    public String generateToken(UserDetails userDetails,String role){
        Map<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24*365) )
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
        
        
    }
    public String generateTokenWithoutExtraClaims(UserDetails user){

        return generateToken(user, null);
    }

    public Claims extractAllClaims(String token){
        return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }
    private <T> T extractClaim(String claim, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(claim);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public boolean isTokenExpired(String token){
        Date exp = extractClaim(token, Claims::getExpiration);
        Date current = new Date(System.currentTimeMillis());
        return exp.before(current);
    }
    public boolean isTokenValid(String token, UserDetails user){
        String toke = extractUsername(token);
        return (toke.equalsIgnoreCase(user.getUsername()) && !isTokenExpired(token));
    }
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

