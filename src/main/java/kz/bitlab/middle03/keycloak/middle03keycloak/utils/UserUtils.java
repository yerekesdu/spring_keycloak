package kz.bitlab.middle03.keycloak.middle03keycloak.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public final class UserUtils {

    public static Jwt getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        }
        log.warn("Couldn't get current user");
        return null;
    }

    public static String getCurrentUsername() {
        Jwt jwt = getCurrentUser();
        if (jwt != null) {
            return jwt.getClaimAsString("given_name");
        }
        return null;
    }

    public static String getCurrentUserLogin() {
        Jwt jwt = getCurrentUser();
        if (jwt != null) {
            return jwt.getClaimAsString("preferred_username");
        }
        return null;
    }

    public static String getCurrentUserFirstName() {
        Jwt jwt = getCurrentUser();
        if (jwt != null) {
            return jwt.getClaimAsString("name");
        }
        return null;
    }

    public static String getCurrentLastName() {
        Jwt jwt = getCurrentUser();
        if (jwt != null) {
            return jwt.getClaimAsString("family_name");
        }
        return null;
    }

    public static String getCurrentUserEmail() {
        Jwt jwt = getCurrentUser();
        if (jwt != null) {
            return jwt.getClaimAsString("email");
        }
        return null;
    }
}
