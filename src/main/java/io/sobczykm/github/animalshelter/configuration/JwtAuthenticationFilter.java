package io.sobczykm.github.animalshelter.configuration;

import io.sobczykm.github.animalshelter.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.sobczykm.github.animalshelter.constant.Constants.*;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            Long employeeId = getEmployeeId(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                if (tokenProvider.isTokeValid(token, employeeId)) {
                    List<GrantedAuthority> authorities = tokenProvider.getAuthoritiesFromToken(token);
                    Authentication authentication = tokenProvider.getAuthentication(employeeId, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }

    private String getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).orElse(EMPTY);
    }

    private Long getEmployeeId(String token) {
        return Long.valueOf(tokenProvider.getSubject(token));
    }
}
