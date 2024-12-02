package kz.bitlab.middle03.keycloak.middle03keycloak.api;

import kz.bitlab.middle03.keycloak.middle03keycloak.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class ItemController {

    @GetMapping(value = "/home")
    @PreAuthorize("isAuthenticated()")
    public String homePage() {
        return "This is home and here is " + UserUtils.getCurrentUsername();
    }

    @GetMapping(value = "/about")
    @PreAuthorize("hasAnyRole('USER')")
    public String aboutPage() {
        return "This is about page here is " + UserUtils.getCurrentUserFirstName() +
                " - " + UserUtils.getCurrentLastName();
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String adminPage() {
        return "This is admin page " + UserUtils.getCurrentUserEmail();
    }
}
