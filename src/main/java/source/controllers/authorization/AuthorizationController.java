package source.controllers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.exception.AccStorageException;

import javax.validation.Valid;

@Controller
public class AuthorizationController {
    @Autowired
    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private final AuthorizationService authorizationService;

    @PostMapping("/sign")
    public String registerUser(@ModelAttribute("account") @Valid Account account,
                               BindingResult bindingResult, @RequestParam String chPass, Model model) throws AccStorageException {

        return authorizationService.registration(account, bindingResult, chPass);
    }

    @GetMapping("/sign")
    public String showSignPage(Model model) {
        model.addAttribute("account", new Account());
        return "sign-in";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout) {

        if (checkAuth()) {
            return "redirect:" + "main";
        }

        authorizationService.checkErrorAndLogout(model, error, logout);

        model.addAttribute("account", new Account());
        return "log-in";
    }

    private boolean checkAuth() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
    }

}
