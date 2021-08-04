package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.controllers.authorization.entity.Account;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class FriendListController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/friend-list")
    public String friendListPage(@SessionAttribute int id, Model model) throws AccStorageException {
        List<Account> allUsers = accountRepository.getFriends(id);

        model.addAttribute("users", allUsers.toArray());
        return "friend-list";
    }
}
