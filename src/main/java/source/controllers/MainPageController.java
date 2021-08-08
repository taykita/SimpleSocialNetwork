package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class MainPageController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("")
    public String redirect(@SessionAttribute Integer id) {
        if (id == null) {
            return "redirect:login";
        }
        return "redirect:main";
    }

    @GetMapping("/main")
    public String mainPage(@SessionAttribute Integer id, Model model) throws AccStorageException {
        if (id == null) {
            return "redirect:login";
        }
        model.addAttribute("post", new Post());

        Account account = accountRepository.get(id);
        List<Post> posts = accountRepository.getPosts(id);
        //Collections.reverse(posts);

        if (posts != null) {
            model.addAttribute("posts", posts);
        }
        model.addAttribute("name", account.getUserName());
        return "main";
    }

}
