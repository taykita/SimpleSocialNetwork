package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.enums.SideMenuEnum;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class ChatListController {
    @Autowired
    public ChatListController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @GetMapping("/chat-list")
    public String chatListPage(@AuthenticationPrincipal User activeUser, Model model) throws AccStorageException {
        Account user = accountRepository.get(activeUser.getId());
        List<Account> allFriends = accountRepository.getFriends(user);

        updateModel(model, allFriends, user.getId());
        return "chat-list";
    }

    private void updateModel(Model model, List<Account> allFriends, int id) {
        model.addAttribute("users", allFriends);
        model.addAttribute("id", id);
        model.addAttribute("active", SideMenuEnum.CHAT);
    }
}
