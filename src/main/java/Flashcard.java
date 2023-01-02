public class Flashcard  {
    private Integer id;
    private String content;

    private String creator;

    public Flashcard() {
    }

    public Flashcard(Integer id, String content, String creator) {
        this.id = id;
        this.content = content;
        this.creator = creator;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
