package de.fnortheim.security;

import de.fnortheim.domain.User;
import de.fnortheim.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private HttpServletRequest request;

    private LoginAttemptService loginAttemptService;

    public DomainUserDetailsService(UserRepository userRepository, HttpServletRequest request, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.request = request;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        String userIp = getClientIp();
        if (loginAttemptService.isBlocked(userIp)) {
            throw new LockedException("blocked");
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userByEmailFromDatabase = userRepository.findOneWithAuthoritiesByEmail(lowercaseLogin);
        return userByEmailFromDatabase.map(user -> createSpringSecurityUser(lowercaseLogin, user)).orElseGet(() -> {
            Optional<User> userByLoginFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);
            return userByLoginFromDatabase.map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                    "database"));
        });
    }

    private String getClientIp() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(".")[0];
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
            user.getPassword(),
            grantedAuthorities);
    }
}
