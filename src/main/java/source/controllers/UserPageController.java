package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserPageController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/user-page")
    public String userPage(@AuthenticationPrincipal Account activeUser,
                           HttpServletRequest request, Model model) throws AccStorageException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (id == activeUser.getId()) {
            return "redirect:main";
        }

        String rawCount = request.getParameter("count");
        int count;
        if (rawCount == null) {
            count = 10;
        } else {
            count = Integer.parseInt(rawCount);
        }

        List<Post> posts = accountRepository.getPosts(id, count);

        model.addAttribute("count", count + 10);
        model.addAttribute("posts", posts);

        model.addAttribute("name", getUserName(id));
        model.addAttribute("isFriend", isFriend(activeUser, accountRepository.get(id)));
        model.addAttribute("id", id);
        return "user-page";
    }

    private boolean isFriend(Account user, Account friend) throws AccStorageException {
        return accountRepository.isFriend(user, friend);
    }

    private String getUserName(Integer id) throws AccStorageException {
        return accountRepository.get(id).getName();
    }

}
