package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.controllers.entity.Account;

import java.util.List;

@Controller
public class UserListController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/user-list")
    public String userListPage(Model model) throws AccStorageException {
        List<Account> allUsers = accountRepository.getAll();
        model.addAttribute("users", allUsers.toArray());
        return "all-users";
    }
}