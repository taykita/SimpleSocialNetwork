package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String newsPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam(required = false, defaultValue = "10") int count,
                           Model model) throws AccStorageException {

        Account user = accountRepository.get(activeUser.getId());

        List<Post> posts = accountRepository.getFriendsPosts(user, count);

        updateModel(model, count, posts);

        return "news";
    }

    private void updateModel(Model model, int count, List<Post> posts) {
        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);
    }

}
