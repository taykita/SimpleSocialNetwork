package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class NewsController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/news")
    public String newsPage(@SessionAttribute int id, Model model) throws AccStorageException {
        List<Post> posts = accountRepository.getFriendsPosts(id);

        model.addAttribute("posts", posts);

        return "news";
    }

}
