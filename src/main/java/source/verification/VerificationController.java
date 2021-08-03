package source.verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.verification.entity.Account;

import javax.servlet.http.HttpSession;

@Controller
public class VerificationController {

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/sign")
    public String registerUser(@ModelAttribute("account") Account account, @RequestParam String chPass, Model model) throws AccStorageException {
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

            getSession().setAttribute("id", account.getId());
            return "redirect:" + "main";
        }
    }

    //TODO Сделать валидатор
    @GetMapping("/sign")
    public String showSignPage(Model model) {
        model.addAttribute("account", new Account());
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("account") Account account) throws AccStorageException {
        return checkAcc(account.getEmail(), account.getPass());
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    private String checkAcc(String email, String password) throws AccStorageException {
        if (accountRepository.exist(email)) {
            if (accountRepository.confirmPass(email, password)) {
                getSession().setAttribute("id", accountRepository.get(email).getId());
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
