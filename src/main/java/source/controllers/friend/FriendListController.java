package source.controllers.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class FriendListController {
    @Autowired
    public FriendListController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @GetMapping("/friend-list")
    public String friendListPage(@AuthenticationPrincipal User activeUser, Model model) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        List<Account> allFriends = accountRepository.getFriends(user);

        updateModel(model, allFriends, user.getId());
        return "friend-list";
    }

    private void updateModel(Model model, List<Account> allFriends, int id) {
        model.addAttribute("users", allFriends);
        model.addAttribute("id", id);
        model.addAttribute("active", SideMenuItems.FRIENDS);
    }
}
