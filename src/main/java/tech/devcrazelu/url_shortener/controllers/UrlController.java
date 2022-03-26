package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import tech.devcrazelu.url_shortener.models.ShortenedUrl;
import tech.devcrazelu.url_shortener.models.requests.CreateShortUrlRequest;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import tech.devcrazelu.url_shortener.services.UrlService;

import java.util.List;
import java.util.UUID;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable String shortUrl){
        RedirectView redirectView = null;
        String longUrl = urlService.getLongUrl(shortUrl);
        if(longUrl != null){
             redirectView = new RedirectView();
            redirectView.setUrl(longUrl);
            urlService.updateClickCount(shortUrl);
        }
        //todo: redirect to a 404 page
        return redirectView;
    }

    @GetMapping("getShortUrl/{shortUrl}")
    public ApiResponse getShortUrl(@PathVariable String shortUrl){
        String longUrl = urlService.getLongUrl(shortUrl);
        if(longUrl!= null) return new ApiResponse(true,longUrl);
        return new ApiResponse("Invalid short url");
    }

    @PostMapping("/createShortUrl")
    public ApiResponse createShortUrl(@RequestBody CreateShortUrlRequest request){
        String shortUrl = urlService.createShortUrl(request.getLongUrl(), UUID.fromString(request.getUserId()));
        if(shortUrl!= null) return new ApiResponse(true, shortUrl);
        return new ApiResponse("Unable to shorten "+request.getLongUrl());
    }

    @GetMapping("/getShortUrls/{userId}")
    public ApiResponse getShortenedUrlsForUser(@PathVariable UUID userId){
        List<ShortenedUrl> shortenedUrls = urlService.getShortenedUrls(userId);
        if(shortenedUrls!= null) return new ApiResponse(true, shortenedUrls);
        return new ApiResponse("Unable to retrieve your short urls");
    }

    @PostMapping("/deleteShortUrl")
    public ApiResponse deleteShortUrl(@RequestBody String shortUrl){
        boolean deleted = urlService.deleteShortUrl(shortUrl);
        return new ApiResponse(deleted, null, deleted ? "Short url deleted": "Unable to delete short url");
    }
}
