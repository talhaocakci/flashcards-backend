import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

public class AuthenticationChecker {

    public String getAuthenticatedUser(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        Claims claims = null;

        try {

            claims = Jwts.parserBuilder()
                    .setSigningKey(LoginApi.key)
                    .build()
                    .parseClaimsJws(authorization)
                    .getBody();
        } catch (Exception e) {

        }

        return claims != null ? claims.getSubject() : null;
    }
}
