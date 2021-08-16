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
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthorizationController {
    @Autowired
    public AuthorizationController(AccountRepository accountRepository, HttpSession session) {
        this.accountRepository = accountRepository;
        this.session = session;
    }

    private final AccountRepository accountRepository;

    private final HttpSession session;

    @PostMapping("/sign")
    public String registerUser(@ModelAttribute("account") @Valid Account account,
                               BindingResult bindingResult, @RequestParam String chPass, Model model) throws AccStorageException {
        if (bindingResult.hasErrors())
            return "sign-in";

        if (account.getPass().equals(chPass)) {
            return registration(account);
        } else {
            return "redirect:" + "sign";
        }
    }

    private String registration(Account account) throws AccStorageException {
        if (accountRepository.exist(account.getEmail())) {
            return "redirect:" + "sign";
        } else {
            accountRepository.add(account);

            session.setAttribute("id", account.getId());
            return "redirect:" + "login";
        }
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
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Account) {
            return "redirect:" + "main";
        }

        if (error != null) {
            model.addAttribute("error", "Неправильное имя пользователя или пароль!");
        }

        if (logout != null) {
            model.addAttribute("logout", "Вы вышли!");
        }

        model.addAttribute("account", new Account());
        return "log-in";
    }

}
