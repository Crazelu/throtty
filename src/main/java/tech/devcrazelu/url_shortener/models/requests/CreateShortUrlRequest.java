package tech.devcrazelu.url_shortener.models.requests;

public class CreateShortUrlRequest {
    private String longUrl;
    private String userId;

    public CreateShortUrlRequest(String longUrl, String userId) {
        this.longUrl = longUrl;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
