import java.util.ArrayList;
import java.util.List;

public class FlashcardRetriever {

    public List<Flashcard> retrieveAll() {
        List<Flashcard> cards = new ArrayList<>();
        cards.add(new Flashcard(1, "Do the best you can until you know better. Then when you know better, do better", "demouser"));
        cards.add(new Flashcard(2, "Almost everything will work again if you unplug it for a few minutes, including you.", "tocakci"));

        return cards;
    }
}
