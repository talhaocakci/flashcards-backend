import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServletContextInitializer implements ServletContextListener {

    AuthenticationChecker authenticationChecker = new AuthenticationChecker();
    FlashcardRetriever flashcardRetriever = new FlashcardRetriever();

    @Override
    public void contextInitialized(ServletContextEvent event) {

        LoginApi loginApi = new LoginApi();
        FlashcardsApi flashcardsApi = new FlashcardsApi(authenticationChecker, flashcardRetriever);

        ServletContext servletContext = event.getServletContext();

        servletContext.addServlet("loginApi", loginApi);
        ServletRegistration.Dynamic registration = servletContext.addServlet("flashcardsApi", flashcardsApi);
        registration.addMapping("/api/flashcards", "/api/flashcards/*");
    }

}
