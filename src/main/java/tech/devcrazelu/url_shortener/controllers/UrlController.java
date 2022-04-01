package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import tech.devcrazelu.url_shortener.models.ShortenedUrl;
import tech.devcrazelu.url_shortener.models.requests.CreateShortUrlRequest;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import tech.devcrazelu.url_shortener.services.UrlService;
import tech.devcrazelu.url_shortener.utils.AuthUtil;
import java.util.List;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;
    @Autowired
    private AuthUtil authUtil;

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

    @GetMapping("getLongUrl/{shortUrl}")
    public ApiResponse getShortUrl(@PathVariable String shortUrl){
            String longUrl = urlService.getLongUrl(shortUrl);
            if (longUrl != null) return new ApiResponse(true, longUrl);
            return new ApiResponse("Invalid short url");
    }

    @PostMapping("/createShortUrl")
    public ApiResponse createShortUrl(@RequestBody CreateShortUrlRequest request){
        int userId = authUtil.getAuthenticatedUserId();
        String shortUrl = urlService.createShortUrl(request.longUrl, userId);
        if (shortUrl != null) return new ApiResponse(true, shortUrl);
        return new ApiResponse("Unable to shorten " + request.longUrl);
    }

    @GetMapping("/getShortUrls")
    public ApiResponse getShortenedUrlsForUser(){
        int userId = authUtil.getAuthenticatedUserId();
        List<ShortenedUrl> shortenedUrls = urlService.getShortenedUrls(userId);
        if (shortenedUrls != null) return new ApiResponse(true, shortenedUrls);
        return new ApiResponse("Unable to retrieve your short urls");
    }

    @DeleteMapping("/deleteShortUrl/{shortUrl}")
    public ApiResponse deleteShortUrl(@PathVariable String shortUrl){
        boolean deleted = urlService.deleteShortUrl(shortUrl);
        return new ApiResponse(deleted, null, deleted ? "Short url deleted": "Unable to delete short url");
    }
}
