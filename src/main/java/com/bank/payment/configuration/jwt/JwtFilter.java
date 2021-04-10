package com.bank.payment.configuration.jwt;

import com.bank.payment.exception.JwtAuthenticationException;
import com.bank.payment.model.TokenEntity;
import com.bank.payment.repository.TokenEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TokenEntityRepository tokenEntityRepository;

    @Value("${jrt.token.key}")
    private String TOKEN_KEY;

    @Value("${jwt.token.header}")
    private String HEADER_VALUE;

    @Override
    public void doFilter (
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain)
            throws IOException, ServletException, JwtAuthenticationException {

        String token = null;
        String bearer = ((HttpServletRequest) servletRequest).getHeader(HEADER_VALUE);
        if (StringUtils.hasText(bearer) && bearer.startsWith(TOKEN_KEY + " ")) {
            token = bearer.substring(7);
        }

        if (token != null && jwtProvider.validateToken(token)) {
            TokenEntity tokenEntity = tokenEntityRepository.findByTokenName(token);
            if (tokenEntity != null && token.equals(tokenEntity.getTokenName())) {
                throw new JwtAuthenticationException("Access denied. Please register or login again!");
            }
            String userLogin = jwtProvider.getLoginFromToken(token);
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
