package source.controllers.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Controller
public class FriendController {
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/add-friend")
    public String addFriend(@RequestParam int id, @AuthenticationPrincipal Account activeUser) throws AccStorageException {
        accountRepository.addFriend(activeUser, accountRepository.get(id));
        return "redirect:" + "user-page?id=" + id;
    }

    @GetMapping("/delete-friend")
    public String deleteFriend(@RequestParam int id, @AuthenticationPrincipal Account activeUser) throws AccStorageException {
        accountRepository.deleteFriend(activeUser, accountRepository.get(id));
        return "redirect:" + "friend-list";
    }

}
