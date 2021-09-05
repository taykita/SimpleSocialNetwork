package source.controllers.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Controller
public class FriendController {
    @Autowired
    public FriendController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;


    @PostMapping("/add-friend")
    public String addFriend(@RequestParam int id, @AuthenticationPrincipal User activeUser) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.addFriend(user, accountRepository.getAccount(id));
        return "redirect:" + "user-page?id=" + id;
    }

    @GetMapping("/delete-friend")
    public String deleteFriend(@RequestParam int id, @AuthenticationPrincipal User activeUser) throws AccStorageException {
        Account user = accountRepository.getAccount(activeUser.getId());
        accountRepository.deleteFriend(user, accountRepository.getAccount(id));
        return "redirect:" + "friend-list";
    }

}
