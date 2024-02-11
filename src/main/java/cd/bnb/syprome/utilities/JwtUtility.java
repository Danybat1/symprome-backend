package cd.bnb.syprome.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable
{
    private static final long serialVersionUID = 2550185165626007490L;
    public static final long JWT_TOKEN_VALIDITY=60*60;
    @Value("{jwt.secret}")
    private String secretKey;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsFunction) {
        final Claims claims = getAllClaimFromToken(token);
        return claimsFunction.apply(claims);
    }

    private Claims getAllClaimFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails u) {
        final Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, u.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims,String subjet) {
        return Jwts.builder().setClaims(claims).setSubject(subjet).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + JwtUtility.JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512,secretKey).compact();
    }

    public Boolean validateToken(String token,UserDetails u) {
        final String username =getUsernameFromToken(token);
        return username.equals(u.getUsername()) && !isExpiredToken(token);
    }

    public Boolean isExpiredToken( String token) {
        final Date expiration =getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }




}
