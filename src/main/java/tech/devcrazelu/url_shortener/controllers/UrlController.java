package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import tech.devcrazelu.url_shortener.models.ShortenedUrl;
import tech.devcrazelu.url_shortener.models.requests.CreateShortUrlRequest;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import tech.devcrazelu.url_shortener.services.UrlService;
import tech.devcrazelu.url_shortener.utils.AuthUtil;
import tech.devcrazelu.url_shortener.validators.RequestValidator;

import java.util.List;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RequestValidator validator;

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
    public ResponseEntity<ApiResponse> getShortUrl(@PathVariable String shortUrl){
            String longUrl = urlService.getLongUrl(shortUrl);
            if (longUrl != null) return new ResponseEntity(new ApiResponse(true, longUrl), HttpStatus.OK);
        return new ResponseEntity(new ApiResponse("Invalid short url"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/createShortUrl")
    public ResponseEntity<ApiResponse> createShortUrl(@RequestBody CreateShortUrlRequest request){
        validator.validateCreateShortUrlRequest(request);
        int userId = authUtil.getAuthenticatedUserId();
        String shortUrl = urlService.createShortUrl(request.longUrl, userId, request.shortUrl);
        if (shortUrl != null) return new ResponseEntity(new ApiResponse(true, shortUrl), HttpStatus.CREATED);
        return new ResponseEntity(new ApiResponse("Unable to shorten " + request.longUrl), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getShortUrls")
    public ResponseEntity<ApiResponse> getShortenedUrlsForUser(){
        int userId = authUtil.getAuthenticatedUserId();
        List<ShortenedUrl> shortenedUrls = urlService.getShortenedUrls(userId);
        if (shortenedUrls != null) return new ResponseEntity(new ApiResponse(true, shortenedUrls), HttpStatus.OK);
       return new ResponseEntity(new ApiResponse("Unable to retrieve your short urls"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/deleteShortUrl/{shortUrl}")
    public ResponseEntity<ApiResponse> deleteShortUrl(@PathVariable String shortUrl){
        boolean deleted = urlService.deleteShortUrl(shortUrl);
        if(deleted) return new ResponseEntity(new ApiResponse(true, null, "Short url deleted"), HttpStatus.OK);
        return new ResponseEntity(new ApiResponse("Unable to delete short url"), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
