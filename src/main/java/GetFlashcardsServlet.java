import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/flashcards")
public class GetFlashcardsServlet extends HttpServlet {

    List<Flashcard> flashCards = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        flashCards.add(new Flashcard(1, "Do the best you can until you know better. Then when you know better, do better"));
        flashCards.add(new Flashcard(2, "Almost everything will work again if you unplug it for a few minutes, including you."));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cardId = request.getParameter("cardId");

        List<Flashcard> flashcardList = new ArrayList<>();

        if (cardId != null) {
            Integer flashcardId;

            try {
                flashcardId = Integer.parseInt(cardId);

                Optional<Flashcard> cardO =
                        flashCards.stream().filter(c -> flashcardId.equals(c.getId())).findFirst();

                if (cardO.isPresent()) {
                    flashcardList.add(cardO.get());
                    request.setAttribute("cards", flashcardList);
                } else {
                    request.setAttribute("error", String.format("Flash card could not be found by id %s", flashcardId));
                }

            }catch (NumberFormatException e) {
                request.setAttribute("error", String.format("Id is not acceptable %s", cardId));
            }
        } else {

            request.setAttribute("cards", flashCards);
        }

        response.setContentType("text/html");
        getServletContext().getRequestDispatcher("/flashcard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String content = request.getParameter("content");

        Integer newId = flashCards.get(flashCards.size() - 1).getId();

        var flashCard = new Flashcard(newId, content);

        flashCards.add(flashCard);

        request.setAttribute("cards", flashCards);

        request.setAttribute("message", "Your flashcard has been created");

        getServletContext().getRequestDispatcher("/flashcard.jsp").forward(request, response);
    }
}
