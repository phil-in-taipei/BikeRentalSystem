package BikeRentalSystem.controllers;
import BikeRentalSystem.models.UserPrincipal;
import BikeRentalSystem.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private final CustomUserService customUserService;

    public AdminController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @GetMapping("/admin")
    public String viewHomePage(Model model) {
        final List<UserPrincipal> customerList = customUserService.getAllCustomers();//customUserService.getAllUsers();
        model.addAttribute("customerList", customerList);
        return "authentication/admin";
    }



}
