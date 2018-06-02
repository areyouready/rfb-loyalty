package de.fnortheim.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * @author sebastianbasner
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    LoginAttemptService loginAttempService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        loginAttempService.loginSucceeded(webAuthenticationDetails.getRemoteAddress());
    }
}
