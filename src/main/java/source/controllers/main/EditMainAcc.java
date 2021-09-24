package source.controllers.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.exception.AccStorageException;

@Controller
public class EditMainAcc {
    @Autowired
    public EditMainAcc(MainService mainService) {
        this.mainService = mainService;
    }

    private final MainService mainService;

    @GetMapping("/edit-acc")
    public String editAccPage(@AuthenticationPrincipal User activeUser,
                              Model model) throws AccStorageException {

        Account currentAccount = mainService.getAccount(activeUser);
        model.addAttribute("account", currentAccount);
        model.addAttribute("newAccount", new Account());

        return "edit-acc";
    }

    @PostMapping("/edit-acc")
    public String editAcc(@AuthenticationPrincipal User activeUser,
                          @ModelAttribute("newAccount") Account newAccount,
                          @RequestParam(required = false) String chPass,
                          @RequestParam(required = false) String oldPass) throws AccStorageException {

        mainService.checkAndUpdate(activeUser, newAccount, chPass, oldPass);

        return "redirect:main";
    }

}
