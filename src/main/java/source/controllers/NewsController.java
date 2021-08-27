package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class NewsController {
    @Autowired
    public NewsController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;


    @GetMapping("/news")
    public String newsPage(@AuthenticationPrincipal User activeUser, Model model) {
        model.addAttribute("id", activeUser.getId());
        model.addAttribute("isMain", false);
        model.addAttribute("isChat", false);
        model.addAttribute("isNews", true);
        model.addAttribute("isFriends", false);
        model.addAttribute("isUsers", false);
        return "news";
    }

    @GetMapping("/news/get-posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        int id = activeUser.getId();
        return accountRepository.getFriendsPosts(accountRepository.get(id), firstPostId, 10);
    }

}
