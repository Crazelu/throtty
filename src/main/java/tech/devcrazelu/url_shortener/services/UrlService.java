package tech.devcrazelu.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.models.ShortenedUrl;
import tech.devcrazelu.url_shortener.repositories.UrlRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public String createShortUrl(String longUrl, int userId, String shortUrl){
        if(longUrl.isEmpty())return null;

        boolean created = false;
        int tries = 10;

        while(!created && tries>0){
            if(shortUrl == null) shortUrl = randomShortUrl();
            created = urlRepository.createShortUrl(shortUrl, longUrl, userId);
            tries--;
        }
        return created ? shortUrl: null;
    }

   public String getLongUrl(String shortUrl){
        return urlRepository.getLongUrl(shortUrl);
    }

    public boolean deleteShortUrl(String shortUrl){
        return urlRepository.deleteShortUrl(shortUrl);
    }

    public List<ShortenedUrl> getShortenedUrls(int userId){
        return urlRepository.getShortUrlsForUser(userId);
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

    private String randomShortUrl(){
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
