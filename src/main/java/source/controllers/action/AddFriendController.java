package source.controllers.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.database.AccountRepository;
import source.exception.AccStorageException;

@Controller
public class AddFriendController {
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("add-friend")
    public String addFriend(@RequestParam int id, @SessionAttribute int userId) throws AccStorageException {
        accountRepository.addFriend(userId, id);
        return "redirect:" + "user-page?id=" + id;
    }

}
