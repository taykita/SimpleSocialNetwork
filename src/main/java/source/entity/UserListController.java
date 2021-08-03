package source.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.database.AccountRepository;
import source.exception.AccStorageException;
import source.verification.entity.Account;

import java.util.List;

@Controller
public class UserListController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/user-list")
    public String userListPage(Model model) throws AccStorageException {
        List<Account> allUsers = getAllUsers();
        model.addAttribute("users", allUsers.toArray());
        return "all-users";
    }

    private List<Account> getAllUsers() throws AccStorageException {
        List<Account> allUsers;
        allUsers = accountRepository.getAll();
        return allUsers;
    }
}
