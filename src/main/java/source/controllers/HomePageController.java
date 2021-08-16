package source.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.User;

@Controller
public class HomePageController {

    @GetMapping("")
    public String homePage(Model model) {

        updateModel(model);

        return "home";
    }

    private void updateModel(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            model.addAttribute("isAuth", true);
        } else {
            model.addAttribute("isAuth", false);
        }
    }


}
