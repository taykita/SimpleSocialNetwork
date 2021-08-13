package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class NewsController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/news")
    public String newsPage(@AuthenticationPrincipal Account activeUser, Model model) throws AccStorageException {
        List<Post> posts = accountRepository.getFriendsPosts(activeUser);

        model.addAttribute("posts", posts);

        return "news";
    }

}
