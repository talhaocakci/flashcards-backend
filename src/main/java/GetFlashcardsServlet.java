import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/flashcards")
public class GetFlashcardsServlet extends HttpServlet {

    Map<Integer, String> flashCards = new HashMap<Integer, String>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        flashCards.put(1, "Do the best you can until you know better. Then when you know better, do better");
        flashCards.put(2, "Almost everything will work again if you unplug it for a few minutes, including you.");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cardId = request.getParameter("cardId");

        if (cardId != null) {
            Integer flashcardId;

            try {
                flashcardId = Integer.parseInt(cardId);

                if (flashCards.containsKey(flashcardId)) {
                    request.setAttribute("content", flashCards.get(flashcardId));
                    request.setAttribute("id", flashcardId);
                } else {
                    request.setAttribute("error", String.format("Flash card could not be found by id %s", flashcardId));
                }

            }catch (NumberFormatException e) {
                request.setAttribute("error", String.format("Id is not acceptable %s", cardId));
            }
        } else {
            request.setAttribute("error", "Please provide a card id");
        }

        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/flashcard.jsp").forward(request, response);
    }
}
