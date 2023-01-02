import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class AuthenticationChecker {

    public String getAuthenticatedUser(HttpServletRequest request, String secretKey) {
        String authorization = request.getHeader("Authorization");

        Claims claims = null;

        try {

            SecretKey sharedSecret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            claims = Jwts.parserBuilder()
                    .setSigningKey(sharedSecret)
                    .build()
                    .parseClaimsJws(authorization)
                    .getBody();
        } catch (Exception e) {

        }

        return claims != null ? claims.getSubject() : null;
    }
}
