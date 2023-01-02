import jakarta.validation.constraints.NotBlank;

public class FlashcardPostRequest {

    private Integer id;

    @NotBlank(message = "Please provide the content")
    private String content;

    public FlashcardPostRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
