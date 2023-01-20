package BikeRentalSystem.controllers;
import BikeRentalSystem.models.UserMeta;
import BikeRentalSystem.models.UserPrincipal;
import BikeRentalSystem.services.CustomUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    CustomUserService userDetailService;
    @GetMapping("/")
    public String homePage(Authentication authentication, Model model) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        System.out.println("This is the user: " + user);
        model.addAttribute("user", user);
        return "authentication/home";
    }

    //@GetMapping("/admin")
    //public String adminPage() {
    //    return "authentication/admin";
   // }

    @RequestMapping("/signin")
    public String showLoginPage() {
            return "authentication/signin";
    }



        @GetMapping("/customer")
    public ModelAndView showUserMetaEditPage(Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        ModelAndView mav = new ModelAndView("authentication/edit-user-meta");
        UserMeta userMeta = user.getUserMeta();
        if (userMeta == null) {
            mav.setViewName("error");
            mav.addObject("message",
                    "Customer with id "
                            + user.getId() + " does not exist."
            );
        } else {
            mav.addObject("userMeta", userMeta);
        }
        return mav;//"authentication/customer";
    }

    @PreAuthorize("#id == authentication.principal.userMeta.id")
    @PostMapping("/update-user-meta/{id}")
    public String updateUserMeta(
            @PathVariable(name = "id") Long id,
            @ModelAttribute("userMeta") UserMeta userMeta, Model model) {
        if (!id.equals(userMeta.getId())) {
            model.addAttribute("message",
                    "Cannot update, customer id " + userMeta.getId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error";
        }
        userDetailService.updateUserMeta(userMeta);
        return "redirect:/";
    }
}
