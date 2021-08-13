package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class MainPageController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("")
    public String redirect() {
        return "redirect:main";
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal Account activeUser, Model model) throws AccStorageException {
        model.addAttribute("post", new Post());

        List<Post> posts = accountRepository.getPosts(activeUser.getId());

        if (posts != null) {
            model.addAttribute("posts", posts);
        }
        model.addAttribute("name", activeUser.getUsername());
        return "main";
    }

}
