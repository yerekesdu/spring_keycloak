package kz.bitlab.middle03.keycloak.middle03keycloak.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

public class KeyCloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorityCollection = extractAuthorities(source);
        System.out.println("authorityCollection: " + authorityCollection);
        return new JwtAuthenticationToken(source, authorityCollection);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt source) {
        Map<String, Object> claims = (Map<String, Object>) source.getClaims().get("realm_access");
        if (claims == null || claims.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> roles = (List<String>) claims.get("roles");

        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}