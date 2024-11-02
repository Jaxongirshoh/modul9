package dev.wisespirit.modul9.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtil {

    private final Key signingKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;
    private final String issuer;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(@Value("${jwt.secret.key}") Key signingKey,
                   @Value("${jwt.access-token.ttl}") long accessTokenValiditySeconds,
                   @Value("${jwt.refresh-token.ttl}") long refreshTokenValiditySeconds,
                   @Value("${jwt.issuer}") String issuer) {
        this.signingKey = signingKey;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.issuer = issuer;
    }

    public String generateAccessToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + accessTokenValiditySeconds * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .claims(claims)
                .compact();
    }

    public String generateRefreshToken(String username,Map<String,Object> claims) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValiditySeconds * 1000))
                .signWith(signingKey, SignatureAlgorithm.ES256)
                .claims(claims)
                .compact();
    }

    public boolean isValid(String token){
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }
    public String getUsername(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
