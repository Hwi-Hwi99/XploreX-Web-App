import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY_BASE64 = "xv6LGd2Ho5SBiC6vw7HzwcYYd96QD/x5hVzT8hccbvw=";
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_BASE64));

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10 phút
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractEmail(token).equals(userDetails.getUsername());
    }
}