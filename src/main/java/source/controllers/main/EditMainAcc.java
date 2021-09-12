package source.controllers.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Controller
public class EditMainAcc {
    @Autowired
    public EditMainAcc(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @GetMapping("/edit-acc")
    public String editAccPage(@AuthenticationPrincipal User activeUser,
                           Model model) throws AccStorageException {

        Account currentAccount = accountRepository.getAccount(activeUser.getId());
        model.addAttribute("account", currentAccount);
        model.addAttribute("newAccount", new Account());

        return "edit-acc";
    }

    @PostMapping("/edit-acc")
    public String editAcc(@AuthenticationPrincipal User activeUser,
                          @ModelAttribute("newAccount") Account newAccount,
                          @RequestParam(required = false) String chPass,
                          @RequestParam(required = false) String oldPass) throws AccStorageException {
        Account currentAccount = accountRepository.getAccount(activeUser.getId());

        boolean isEdit = false;

        if (!currentAccount.getName().equals(newAccount.getName())) {
            currentAccount.setName(newAccount.getName());
            isEdit = true;
        }
        if (!currentAccount.getEmail().equals(newAccount.getEmail())) {
            if (!accountRepository.existAccount(newAccount.getEmail())) {
                currentAccount.setEmail(newAccount.getEmail());
                isEdit = true;
            }
        }
        //TODO Как сделать проверку?
        if (!newAccount.getPass().equals("") &&
                currentAccount.getPass().equals(passwordEncoder.encode(oldPass)) &&
                newAccount.getPass().equals(chPass)) {
            currentAccount.setPass(passwordEncoder.encode(newAccount.getPass()));
            isEdit = true;
        }
        if (isEdit) {
            accountRepository.updateAccount(currentAccount);
        }

        return "redirect:main";
    }
}
