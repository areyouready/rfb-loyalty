package de.fnortheim.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sebastianbasner
 */
public class RfbAjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    String errorMessage = "Authentication failed, check your credentials and try again";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if(exception.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = "You have been blocked for 10 invalid login attempts";
        }

        response.sendError(401, errorMessage);
    }
}
