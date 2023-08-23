package com.example.jwt_configuration;

import com.example.model.entity.Token;
import com.example.model.entity.User;
import com.example.model.exception.NotFoundException;
import com.example.model.exception.UnauthorizedException;
import com.example.model.repository.TokenRepository;
import com.example.model.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.List;
@Service
@RequiredArgsConstructor
@Data
public class JwtService {
    public final String KEY = "bsAdy0GtgoriYYoQ7ANbngdz0UQozgGEPAr00+F48iUw2/OJnzj6ijfAt8Y8yPH2";
    private final TokenRepository tokenRepository;
//    private final HandlerExceptionResolver handlerExceptionResolver;
    public String extractEmail(String jwt){
        return extractClaim(jwt,Claims::getSubject);
    }
    private Claims extractAllClaims(String token){

                return Jwts.parserBuilder()
                        .setSigningKey(getSignInKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();


    }
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*20))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractEmail(token);
        Token userToken = tokenRepository.findTokenByToken(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && userToken.getIsExpired().equals("false")
                && userToken.getIsRevoked().equals("false");


    }
    public void throwExc(String message){
        throw new UnauthorizedException(message);
    }
}
