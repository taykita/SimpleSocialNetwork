package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class FriendListController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/friend-list")
    public String friendListPage(@AuthenticationPrincipal Account activeUser, Model model) throws AccStorageException {
        List<Account> allUsers = accountRepository.getFriends(activeUser);

        model.addAttribute("users", allUsers.toArray());
        return "friend-list";
    }
}
