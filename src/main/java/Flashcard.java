public class Flashcard  {
    private Integer id;
    private String content;

    public Flashcard(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
