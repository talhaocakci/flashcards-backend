import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/api/login")
public class LoginApi extends HttpServlet {

    List<User> userList = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        userList.add(new User("tocakci", "12345"));
        userList.add(new User("demouser", "12345"));
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

        if (userO.isPresent()) {
            request.getSession().setAttribute("username", userO.get().getUserName());
            response.setStatus(200);
            message = "You are logged in as " + userO.get().getUserName();
        } else {
            response.setStatus(403);
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        BasicResponse basicResponse = new BasicResponse(message);
        String responseString = objectMapper.writeValueAsString(basicResponse);
        out.print(responseString);
        out.flush();
    }

}
