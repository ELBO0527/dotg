package elbo.dotg.api17.config.security;

import elbo.dotg.api17.service.security.PrincipalUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key}") String secretKey;
    private final PrincipalUserDetailsService principalUserDetailsService;
    private final static long accessTokenValidTime = 1000L * 60 * 60 * 6; //6시간
    private final static long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 14; //14일

    public JwtToken createToken(Authentication authentication){
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .setIssuer("LBW")
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .compact();

        return new JwtToken("Bearer", accessToken, refreshToken);
    }

    public Authentication getAuthentication(String accessToken){
        UserDetails principal = principalUserDetailsService.loadUserByUsername(this.getUserName(accessToken));
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public String getUserName(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            e.getStackTrace();
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
