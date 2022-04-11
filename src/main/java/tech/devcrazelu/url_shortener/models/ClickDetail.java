package tech.devcrazelu.url_shortener.models;

public class ClickDetail {
    private String flag;
    private String country;
    private int clicks;
    private String shortUrl;
    private int id;

    public String getFlag() {
        return flag;
    }

    public String getCountry() {
        return country;
    }

    public int getClicks() {
        return clicks;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public int getId() {
        return id;
    }

    public ClickDetail(String flag, String country, int clicks, String shortUrl) {
        this.flag = flag;
        this.country = country;
        this.clicks = clicks;
        this.shortUrl = shortUrl;
    }

    public ClickDetail(int id, String flag, String country, int clicks, String shortUrl) {
        this.id = id;
        this.flag = flag;
        this.country = country;
        this.clicks = clicks;
        this.shortUrl = shortUrl;
    }
}
