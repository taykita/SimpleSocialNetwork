package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class UserListController {
    @Autowired
    public UserListController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @GetMapping("/user-list")
    public String userListPage(@AuthenticationPrincipal User activeUser, Model model) throws AccStorageException {
        List<Account> allUsers = accountRepository.getAll();
        model.addAttribute("users", allUsers.toArray());
        model.addAttribute("id", activeUser.getId());
        return "all-users";
    }
}
