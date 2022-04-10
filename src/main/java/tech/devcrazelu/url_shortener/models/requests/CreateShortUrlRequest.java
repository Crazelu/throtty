package tech.devcrazelu.url_shortener.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateShortUrlRequest {
    public String longUrl;
    public String shortUrl;

    public CreateShortUrlRequest(
            @JsonProperty("longUrl") String longUrl,
            @JsonProperty("shortUrl") String shortUrl
                                 ) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}
