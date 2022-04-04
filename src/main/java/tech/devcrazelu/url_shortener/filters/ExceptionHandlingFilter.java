package tech.devcrazelu.url_shortener.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.devcrazelu.url_shortener.exceptions.ApiException;
import tech.devcrazelu.url_shortener.exceptions.AuthorizationException;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (ApiException exception) {
              ApiResponse error = new ApiResponse(exception.getMessage());
             setErrorResponse(response, exception.getStatus(), error);
          }catch (AuthorizationException exception){
            ApiResponse error = new ApiResponse(exception.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, error);
        } catch (MalformedJwtException exception){
            ApiResponse error = new ApiResponse("Malformed JWT");
            setErrorResponse(response, HttpStatus.BAD_REQUEST, error);
        }

    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, Object error){
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write(convertObjectToJson(error));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
