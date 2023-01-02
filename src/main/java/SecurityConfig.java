import com.fasterxml.jackson.annotation.JsonProperty;

public class SecurityConfig {

    @JsonProperty("secret")
    String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
