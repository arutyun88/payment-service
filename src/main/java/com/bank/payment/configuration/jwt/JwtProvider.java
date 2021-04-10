package com.bank.payment.configuration.jwt;

import com.bank.payment.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Log
public class JwtProvider {
    private final HttpServletRequest request;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expired}")
    private long expiredDay;

    @Value("${jwt.token.header}")
    private String HEADER_VALUE;

    @Value("${jrt.token.key}")
    private String TOKEN_KEY;

    public JwtProvider(HttpServletRequest request) {
        this.request = request;
    }

    public String generateToken(String login) {
        Date date = Date.from(LocalDate.now().plusDays(expiredDay).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
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

    public String getTokenFromUsername() {
        String token = null;
        String bearer = request.getHeader(HEADER_VALUE);
        if (StringUtils.hasText(bearer) && bearer.startsWith(TOKEN_KEY + " ")) {
            token =  bearer.substring(7);
        }
        return token;
    }

}
