package io.sobczykm.github.animalshelter.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.sobczykm.github.animalshelter.domain.EmployeePrincipal;
import io.sobczykm.github.animalshelter.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.sobczykm.github.animalshelter.constant.Constants.*;
import static java.lang.System.currentTimeMillis;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    private final EmployeeService employeeService;

    public String createAccessToken(EmployeePrincipal employeePrincipal) {
        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("type", "JWT")
                .and()
                .subject(String.valueOf(employeePrincipal.getEmployee().getEmployeeId()))
                .claim(AUTHORITIES, getAuthoritiesFromPrincipal(employeePrincipal))
                .issuedAt(new Date())
                .expiration(new Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getTokenClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokeValid(String token, Long employeeId) {
        return Long.valueOf(getTokenClaims(token).getPayload().getSubject()).equals(employeeId) && !isTokenExpired(token);
    }

    public String getSubject(String token) {
        return getTokenClaims(token).getPayload().getSubject();
    }

    Jws<Claims> getTokenClaims(String token) {
        Jws<Claims> jwt;
        try {
            jwt = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException ex) {
            throw new JwtException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return jwt;
    }

    public Authentication getAuthentication(Long employeeId,
                                            List<GrantedAuthority> authorities,
                                            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken employeeAuthToken = new UsernamePasswordAuthenticationToken(
                employeeService.getEmployeeById(employeeId),
                null,
                authorities);
        employeeAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return employeeAuthToken;
    }

    private String[] getAuthoritiesFromPrincipal(EmployeePrincipal employeePrincipal) {
        return employeePrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Object authorities = getTokenClaims(token).getPayload().get(AUTHORITIES);
        Collection<String> authoritiesList = (Collection<String>) authorities;
        return authoritiesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}