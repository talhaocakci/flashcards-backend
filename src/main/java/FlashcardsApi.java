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

@WebServlet(urlPatterns = "/api/flashcards")
public class FlashcardsApi extends HttpServlet {

    List<Flashcard> flashCards = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

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

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        String responseString = objectMapper.writeValueAsString(flashCards);

        out.print(responseString);

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (null == request.getSession().getAttribute("username")) {

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(403);

            String responseString = objectMapper.writeValueAsString(new BasicResponse("Please log in to create cards"));

            out.print(responseString);

            out.flush();

            return;
        }

        Flashcard flashcard = objectMapper.readValue(request.getInputStream(), Flashcard.class);

        Integer newId = flashCards.get(flashCards.size() - 1).getId();

        flashcard.setId(newId);

        flashcard.setCreator((String)request.getSession().getAttribute("username"));

        flashCards.add(flashcard);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        String responseString = objectMapper.writeValueAsString(new BasicResponse("Card has been created"));

        out.print(responseString);

        out.flush();
    }
}
