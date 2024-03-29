package source.controllers.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
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
                               BindingResult bindingResult, @RequestParam String chPass, Model model)
            throws AccStorageException, JsonProcessingException {
        if (bindingResult.hasErrors())
            return "sign-in";

        if (authorizationService.registration(account, chPass)) {
            return "redirect:login";
        }
        return "redirect:sign";
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
            return "redirect:main";
        }

        if (error != null) {
            model.addAttribute("error", "Неправильное имя пользователя или пароль!");
        }

        if (logout != null) {
            model.addAttribute("logout", "Вы вышли!");
        }

        return "log-in";
    }

    private boolean checkAuth() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
    }

}
