package BikeRentalSystem.controllers;
import BikeRentalSystem.models.RentalBike;
import BikeRentalSystem.models.UserPrincipal;
import BikeRentalSystem.services.BikeService;
import BikeRentalSystem.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@Controller
public class BikeController {
    @Autowired
    BikeService bikeService;

    @Autowired
    CustomUserService customUserService;

    @GetMapping("/bikes/assign/{userId}")
    public String assignBikeToCustomer(@PathVariable(name = "userId") Long userId, Model model) {
        UserPrincipal user = customUserService.getUser(userId);
        List<RentalBike> bikeList = bikeService.getAvailableBikes();
        model.addAttribute("user", user);
        model.addAttribute("bikeList", bikeList);
        return "bikes/assign-bike";
    }

    @PostMapping("/bikes/assign")
    public String saveBikeToCustomerAssignment(
            @RequestParam("userId") Long userId,
            @RequestParam("bikeId") Long bikeId) {
        RentalBike bike = bikeService.getBike(bikeId);
        bike.setUser(customUserService.getUser(userId));
        bikeService.saveBike(bike);
        return "redirect:/admin";
    }

    @RequestMapping("/unassign/{bikeId}")
    public String unassignBikeFromCustomer(@PathVariable(name = "bikeId") Long bikeId) {
        RentalBike bike = bikeService.getBike(bikeId);
        bike.setUser(null);
        bikeService.saveBike(bike);
        return "redirect:/admin";
    }

    @GetMapping("/bikes")
    public String viewAllBikes(Model model) {
        final List<RentalBike> bikeList = bikeService.getAllBikes();
        model.addAttribute("bikeList", bikeList);
        return "bikes/bikes";
    }

    @GetMapping("/new-bike")
    public String showSubmitBikeFormPage(Model model) {
        RentalBike bike = new RentalBike();
        model.addAttribute("bike", bike);
        return "bikes/new-bike";
    }

    @PostMapping("/bikes")
    public String saveNewBike(@ModelAttribute("bike") RentalBike bike, Model model) {
        try {
            bikeService.saveBike(bike);
        } catch (IllegalArgumentException e) {
            model.addAttribute(
                    "message",
                    "Could not save bike, "
                            + e.getMessage());
            return "error";
        }
        return "redirect:/bikes";
    }
}
