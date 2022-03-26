package tech.devcrazelu.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.models.ShortenedUrl;
import tech.devcrazelu.url_shortener.repositories.UrlRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public String createShortUrl(String longUrl, UUID userId){
        boolean created = false;
        String shortUrl = null;

        while(!created){
             shortUrl = shortenUrl();
            created = urlRepository.createShortUrl(shortUrl, longUrl, userId.toString());
        }
        return shortUrl;
    }

   public String getLongUrl(String shortUrl){
        return urlRepository.getLongUrl(shortUrl);
    }

    public boolean deleteShortUrl(String shortUrl){
        return urlRepository.deleteShortUrl(shortUrl);
    }

    public List<ShortenedUrl> getShortenedUrls(UUID userId){
        return urlRepository.getShortUrlsForUser(userId.toString());
    }

    public void updateClickCount(String shortUrl){
        urlRepository.updateClickCount(shortUrl);
    }

    private String getCharacterSet(){
            String str="456789abcdefABCDEFGHIJKLMNghijklmnopqrstuvwxyzOPQRS0123TUVWXYZ";
            List<String> characters = Arrays.asList(str.split(""));
            Collections.shuffle(characters);
            String shuffledString = "";
            for (String character : characters)
            {
                shuffledString += character;
            }
         return shuffledString;
    }

    private String shortenUrl(){
       long curr = System.currentTimeMillis();
        String result = "";
        final String set = getCharacterSet();

        while(curr>0){
            result = result+set.charAt((int) (curr%62));
            curr = curr/62;
        }
        return result;
    }
}