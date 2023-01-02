import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServletContextInitializer implements ServletContextListener {

    AuthenticationChecker authenticationChecker = new AuthenticationChecker();
    FlashcardRetriever flashcardRetriever = new FlashcardRetriever();

    PropertyReader propertyReader = new PropertyReader();

    @Override
    public void contextInitialized(ServletContextEvent event) {

        LoginApi loginApi = new LoginApi(propertyReader);

        FlashcardsApi flashcardsApi = new FlashcardsApi(authenticationChecker, flashcardRetriever, propertyReader);

        ServletContext servletContext = event.getServletContext();

        ServletRegistration.Dynamic loginApiRegistration = servletContext.addServlet("loginApi", loginApi);
        loginApiRegistration.addMapping("/api/login");

        ServletRegistration.Dynamic registration = servletContext.addServlet("flashcardsApi", flashcardsApi);
        registration.addMapping("/api/flashcards", "/api/flashcards/*");
    }

}
