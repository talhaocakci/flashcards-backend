import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    List<User> userList = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        userList.add(new User("tocakci", "12345"));
        userList.add(new User("demouser", "12345"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> userO =
                userList.stream()
                        .filter(u -> u.getUserName().equals(userName) && u.getPassword().equals(password))
                        .findFirst();

        if (userO.isPresent()) {
            request.getSession().setAttribute("username", userName);
            getServletContext().getRequestDispatcher("/WEB-INF/inside.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "Username and password is not correct");
            getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
