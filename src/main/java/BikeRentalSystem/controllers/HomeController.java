package BikeRentalSystem.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homePage() {
        return "authentication/home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "authentication/admin";
    }

    @GetMapping("/customer")
    public String superUPage() {
        return "authentication/customer";
    }
}
