import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

        flashCards.add(new Flashcard(1, "Do the best you can until you know better. Then when you know better, do better", "demouser"));
        flashCards.add(new Flashcard(2, "Almost everything will work again if you unplug it for a few minutes, including you.", "tocakci"));
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

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        final String activeUser = claims != null ? claims.getSubject() : null;

        if (activeUser == null) {

            response.setStatus(403);

            String responseString = objectMapper.writeValueAsString(new BasicResponse("Please log in to create cards"));

            out.print(responseString);

            out.flush();

            return;
        }

        Flashcard inputFlashCard = objectMapper.readValue(request.getInputStream(), Flashcard.class);

        String message = "";

        if (inputFlashCard.getId() == null) {

            // if card id is null, that means a new card will be created

            Integer newId = flashCards.get(flashCards.size() - 1).getId();

            inputFlashCard.setId(newId);

            inputFlashCard.setCreator(activeUser);

            flashCards.add(inputFlashCard);

            response.setStatus(200);

            message = "Card has been created";

        } else {
            // if cardId is given, that means update is requested for this card

            Optional<Flashcard> cardO = flashCards.stream().filter(c -> c.getId().equals(inputFlashCard.getId())).findFirst();

            if (cardO.isPresent()) {
                // card is found, now check if we are allowed to update it
                Flashcard retrievedCard = cardO.get();
                if (retrievedCard.getCreator().equals(activeUser)) {
                    // you are allowed to update
                    retrievedCard.setContent(inputFlashCard.getContent());
                    message = "Card has been updated";
                    response.setStatus(201);
                } else {
                    message = "You tried to update someone else's flash card : " + retrievedCard.getCreator() + " : " + activeUser;
                    response.setStatus(403);
                }
            }
            else {
                // card is not found. we can not update it
                message = "Card is not found";
                response.setStatus(404);
            }
        }

        String responseString = objectMapper.writeValueAsString(new BasicResponse(message));

        out.print(responseString);

        out.flush();
    }
}
