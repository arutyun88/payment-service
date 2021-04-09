package com.bank.payment.configuration.jwt;

import com.bank.payment.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Log
public class JwtProvider {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expired}")
    private long expiredDay;

    public String generateToken(String login) {
        Date date = Date.from(LocalDate.now().plusDays(expiredDay).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.severe("Token expired");
        } catch (UnsupportedJwtException exception) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException exception) {
            log.severe("Malformed jwt");
        } catch (SignatureException exception) {
            log.severe("Invalid signature");
        } catch (Exception exception) {
            log.severe("invalid token");
            throw new JwtAuthenticationException("JWT token is expired or invalid", exception);
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

}
