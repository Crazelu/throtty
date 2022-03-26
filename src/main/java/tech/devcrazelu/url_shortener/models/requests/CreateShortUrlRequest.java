package tech.devcrazelu.url_shortener.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateShortUrlRequest {
    public String longUrl;

    public CreateShortUrlRequest(@JsonProperty("longUrl") String longUrl) {
        this.longUrl = longUrl;
    }
}
