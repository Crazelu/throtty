package tech.devcrazelu.url_shortener.models;

import java.util.UUID;

public class ShortenedUrl {
    String longUrl;
    String shortUrl;
    int clickCount;
    UUID userId;

    public int getClickCount(){return clickCount;}
    public String getLongUrl(){return longUrl;}
    public String getShortUrl() {return shortUrl;}

    public ShortenedUrl(String longUrl, String shortUrl, UUID userId){
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.clickCount = 0;
        this.userId = userId;
    }

  public ShortenedUrl(String longUrl, String shortUrl, int clickCount){
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.clickCount = clickCount;
    }
}
