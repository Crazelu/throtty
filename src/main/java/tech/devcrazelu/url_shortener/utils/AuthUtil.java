package tech.devcrazelu.url_shortener.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.exceptions.AuthorizationException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthUtil {

    public int getAuthenticatedUserId(){
        int userId = -1;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userId = Integer.decode(authentication.getName());
            Logger.getAnonymousLogger().log(Level.INFO, String.valueOf(userId));
        }
        if(userId==-1) throw new AuthorizationException();
        return userId;
    }
}
