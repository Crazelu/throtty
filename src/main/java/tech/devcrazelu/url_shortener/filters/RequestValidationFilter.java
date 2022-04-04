package tech.devcrazelu.url_shortener.filters;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.devcrazelu.url_shortener.exceptions.ApiException;
import tech.devcrazelu.url_shortener.exceptions.AuthorizationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class RequestValidationFilter extends OncePerRequestFilter {
    private HashMap<String, String> validURIPaths ;
    private String[] unAuthenticatedPaths;

    public RequestValidationFilter(){
        unAuthenticatedPaths = new String[]{
                "/createAccount", "/login", "/{shortUrl}"
        };
        validURIPaths = new HashMap<>();
        validURIPaths.put("/getShortUrls", "GET");
        validURIPaths.put("/getLongUrl", "GET");
        validURIPaths.put("/user", "GET");
        validURIPaths.put("/createShortUrl", "POST");
        validURIPaths.put("/deleteShortUrl", "DELETE");
        validURIPaths.put("/deleteUser", "DELETE");
    }

    private void checkForAuthorizationHeader(HttpServletRequest request){
       try{
           String uri = request.getRequestURI();
           if(uri.matches("(/)(.+)")|| Arrays.stream(unAuthenticatedPaths).anyMatch(path -> path.equals(uri))) return;

           String token = request.getHeader("Authorization");

           if(token == null || token.isEmpty()) throw new AuthorizationException();
       }catch(Exception e){
           throw new AuthorizationException();
       }
    }

    private void validatePathWithMatch(String path, String regex, HttpServletRequest request){
        if(path.equals("/"+request.getRequestURI().split("/")[1])){
            String uri = request.getRequestURI();
            boolean hasMatch = uri.matches(regex);

            String method = request.getMethod();

            if(hasMatch){
                //check if path is valid
                if(! (validURIPaths.containsKey(path) && validURIPaths.get(path).equals(method)))
                    throw new ApiException(HttpStatus.METHOD_NOT_ALLOWED, "Cannot "+ String.format("%s %s", method, uri));
            }else{
                throw new ApiException(HttpStatus.METHOD_NOT_ALLOWED, "Cannot "+ String.format("%s %s", method, uri));
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Logger.getAnonymousLogger().log(Level.INFO, request.getRequestURI());
        Logger.getAnonymousLogger().log(Level.INFO, request.getMethod());

        checkForAuthorizationHeader(request);
        validatePathWithMatch("/deleteShortUrl","(/deleteShortUrl/)(.{7})", request );
        validatePathWithMatch("/getLongUrl","(/getLongUrl/)(.{7})", request );
        validatePathWithMatch("/user","(/user)", request );
        validatePathWithMatch("/deleteUser","(/deleteUser)", request );

        filterChain.doFilter(request, response);
    }


}