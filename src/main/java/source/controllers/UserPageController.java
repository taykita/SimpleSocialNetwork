package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import java.util.Collections;
import java.util.List;

@Controller
public class UserPageController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/user-page")
    public String userPage(@RequestParam int id, @AuthenticationPrincipal Account activeUser, Model model) throws AccStorageException {
        if (id == activeUser.getId()) {
            return "redirect:main";
        }
        List<Post> posts = accountRepository.getPosts(id);

        if (posts != null) {
            model.addAttribute("posts", posts);
        }
        model.addAttribute("name", getUserName(id));
        model.addAttribute("isFriend", isFriend(activeUser, accountRepository.get(id)));
        model.addAttribute("id", id);
        return "user-page";
    }

    private boolean isFriend(Account user, Account friend) throws AccStorageException {
        return accountRepository.isFriend(user, friend);
    }

    private String getUserName(Integer id) throws AccStorageException {
        return accountRepository.get(id).getUserName();
    }

}
