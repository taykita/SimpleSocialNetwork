package source.controllers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
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
    AccountRepository accountRepository;
    @Autowired
    HttpSession session;

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
            return "redirect:" + "main";
        }
    }

    @GetMapping("/sign")
    public String showSignPage(Model model) {
        model.addAttribute("account", new Account());
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("account") @Valid Account account,
                            BindingResult bindingResult) throws AccStorageException {
        if (bindingResult.hasFieldErrors("email")
                || bindingResult.hasFieldErrors("pass"))
            return "log-in";

        return checkAcc(account.getEmail(), account.getPass());
    }

    private String checkAcc(String email, String password) throws AccStorageException {
        if (accountRepository.exist(email)) {
            if (accountRepository.confirmPass(email, password)) {
                session.setAttribute("id", accountRepository.get(email).getId());
                return "redirect:" + "main";
            } else {
                return "redirect:" + "login";
            }
        } else {
            return "redirect:" + "login";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("account", new Account());
        return "log-in";
    }

}
