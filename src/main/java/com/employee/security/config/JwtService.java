package com.employee.security.config;

import com.employee.security.model.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.SECRET_KEY}")
    private String secretKey;

    @Value("${jwt.EXPIRATION_TIME}")
    private long expirationTime;

    @Value("${jwt.REFRESH_EXPIRATION_TIME}")
    private long refreshExpirationTime;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String,Object> claims, UserDetails employee) {
       return buildToken(claims,employee,expirationTime);
    }

    public String generateRefreshToken(Map<String,Object> claims, UserDetails employee) {
        return buildToken(claims,employee,refreshExpirationTime);
    }

    private String buildToken(Map<String,Object> claims, UserDetails employee,long expirationTime){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + expirationTime);
        claims.put("issuedAt", now);
        claims.put("expiration", expiryDate);

        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(employee.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .and()
                .signWith(generateKey())
                .compact();
    }

    public SecretKey generateKey(){
        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
