import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LoginApi extends HttpServlet {
    List<User> userList = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    PropertyReader<SecurityConfig> propertyReader;

    SecurityConfig securityConfig;

    @Override
    public void init(ServletConfig config) throws ServletException {

        userList.add(new User("tocakci", "12345"));
        userList.add(new User("demouser", "12345"));

        securityConfig = propertyReader.getConfiguration("security.yaml", SecurityConfig.class);
    }

    public LoginApi(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User postedUser = objectMapper.readValue(request.getInputStream(), User.class);

        Optional<User> userO =
                userList.stream()
                        .filter(u -> u.getUserName().equals(postedUser.getUserName())
                                && u.getPassword().equals(postedUser.getPassword()))
                        .findFirst();

        String message = "User name or password is wrong";

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (userO.isPresent()) {

            SecretKey sharedSecret = Keys.hmacShaKeyFor(securityConfig.getSecret().getBytes(StandardCharsets.UTF_8));

            String jws = Jwts.builder()
                    .setIssuer("javathlon")
                    .setSubject(userO.get().getUserName())
                    .claim("scope", "admin")
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(Instant.now().plusSeconds(3000)))
                    .signWith(sharedSecret)
                    .compact();

            response.setStatus(200);

            LoginResponse loginResponse = new LoginResponse(jws);
            String responseString = objectMapper.writeValueAsString(loginResponse);
            out.print(responseString);
            out.flush();

            return;
        }

        response.setStatus(403);

        BasicResponse basicResponse = new BasicResponse(message);
        String responseString = objectMapper.writeValueAsString(basicResponse);
        out.print(responseString);
        out.flush();
    }

}
