package tech.devcrazelu.url_shortener.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthUtil {

    public UUID getAuthenticatedUserId(){
        String userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userId = authentication.getName();
            return UUID.fromString(userId);
        }
        return null;
    }
}
